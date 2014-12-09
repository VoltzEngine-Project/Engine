package resonant.lib.grid

import java.util
import java.util.Collections
import java.util.concurrent.ConcurrentLinkedQueue

import cpw.mods.fml.common.eventhandler.{Event, SubscribeEvent}
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import net.minecraftforge.common.MinecraftForge
import resonant.api.IUpdate

import scala.collection.convert.wrapAll._

/**
 * A ticker to update all grids. This is multi-threaded based on configuration.
 *
 * @author Calclavia
 */
object UpdateTicker
{
  final val threaded = new UpdateTicker
  final val world = new UpdateTicker
}

class UpdateTicker extends Thread
{
  setName("Universal Electricity")
  setPriority(Thread.MIN_PRIORITY)

  /**
   * For updaters to be ticked.
   */
  private final val updaters: java.util.Set[IUpdate] = Collections.newSetFromMap(new util.WeakHashMap[IUpdate, java.lang.Boolean]())

  private final val queue = new ConcurrentLinkedQueue[() => Unit]()

  /**
   * For queuing Forge events to be invoked the next tick.
   */
  private final val queuedEvents = new ConcurrentLinkedQueue[Event]()

  /**
   * Becomes true if the network needs to be paused.
   */
  var pause = false

  /**
   * The time in milliseconds between successive updates.
   */
  private var deltaTime = 0L

  def addUpdater(updater: IUpdate)
  {
    enqueue(() => updaters.add(updater))
  }

  def enqueue(f: () => Unit)
  {
    queue.add(f)
  }

  def queueEvent(event: Event)
  {
    queuedEvents.add(event)
  }

  def getDeltaTime = deltaTime

  def getUpdaterCount = updaters.size

  override def run()
  {
    var last = System.currentTimeMillis()

    while (true)
    {
      if (!pause)
      {
        val current = System.currentTimeMillis()
        deltaTime = current - last
        update()

        last = current
      }

      Thread.sleep(50L)
    }
  }

  @SubscribeEvent
  def tickEnd(event: TickEvent.ServerTickEvent)
  {
    if(event.phase == Phase.END)
    {
      deltaTime = 50
      update()
    }
  }

  def update()
  {
    try
    {
      queue synchronized
      {
        queue.foreach(_.apply())
        queue.clear()
      }

      updaters synchronized
      {
        if (this == UpdateTicker.threaded)
          updaters.par.filter(_.canUpdate()).foreach(_.update(getDeltaTime / 1000d))
        else
          updaters.filter(_.canUpdate()).foreach(_.update(getDeltaTime / 1000d))

        updaters.removeAll(updaters.filterNot(_.continueUpdate()))
      }

      queuedEvents synchronized
      {
        queuedEvents.foreach(MinecraftForge.EVENT_BUS.post)
        queuedEvents.clear()
      }
    }
    catch
      {
        case e: Exception =>
        {
          System.out.println("Universal Electricity Ticker: Failed while ticking updaters. This is a bug! Clearing all tickers for self repair.")
          updaters.clear()
          e.printStackTrace()
        }
      }
  }
}
