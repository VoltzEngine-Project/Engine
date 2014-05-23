package universalelectricity.core.grid

import cpw.mods.fml.common.eventhandler.{SubscribeEvent, Event}
import net.minecraftforge.common.MinecraftForge
import java.util._
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.mutable
import cpw.mods.fml.common.gameevent.TickEvent

/**
 * A ticker to update all grids. This is multithreaded.
 *
 * @author Calclavia
 */
object UpdateTicker extends Thread
{
  setName("Universal Electricity")
  setPriority(Thread.MIN_PRIORITY)

  /**
   * For updaters to be ticked.
   */
  private final val updaters = Collections.newSetFromMap(new WeakHashMap[IUpdate, java.lang.Boolean])
  /**
   * For queuing Forge events to be invoked the next tick.
   */
  private final val queuedEvents = new ConcurrentLinkedQueue[Event]

  /**
   * Becomes true if the network needs to be paused.
   */
  var pause = false

  var useThreads = true

  /**
   * The time in milliseconds between successive updates.
   */
  private var deltaTime: Long = 0L

  def addNetwork(updater: IUpdate)
  {
    updaters synchronized
      {
        updaters.add(updater)
      }
  }

  def queueEvent(event: Event)
  {
    queuedEvents synchronized
      {
        queuedEvents.add(event)
      }
  }

  def getDeltaTime = deltaTime

  def getUpdaterCount = updaters.size

  override def run
  {
    try
    {
      var last: Long = System.currentTimeMillis

      while (true)
      {
        if (!pause)
        {
          val current: Long = System.currentTimeMillis
          deltaTime = current - last
          updaters synchronized
            {
              update()
            }
          queuedEvents synchronized
            {
              while (!queuedEvents.isEmpty)
              {
                MinecraftForge.EVENT_BUS.post(queuedEvents.poll)
              }
            }
          last = current
        }

        Thread.sleep(50L)
      }
    }
    catch
      {
        case e: Exception =>
        {
          e.printStackTrace
        }
      }
  }

  @SubscribeEvent
  def tickEnd(event: TickEvent.ServerTickEvent)
  {
    updaters synchronized
      {
        update()
      }
    queuedEvents synchronized
      {
        while (!queuedEvents.isEmpty)
        {
          MinecraftForge.EVENT_BUS.post(queuedEvents.poll)
        }
      }
  }

  def update()
  {
    val removeUpdaters: Set[IUpdate] = Collections.newSetFromMap[IUpdate](new WeakHashMap[IUpdate, java.lang.Boolean])
    val updaterIt: Iterator[IUpdate] = new HashSet[IUpdate](updaters).iterator

    try
    {
      while (updaterIt.hasNext)
      {
        val updater: IUpdate = updaterIt.next
        if (updater.canUpdate)
        {
          updater.update(getDeltaTime / 1000f)
        }
        if (!updater.continueUpdate)
        {
          removeUpdaters.add(updater)
        }
      }
      updaters.removeAll(removeUpdaters)
    }
    catch
      {
        case e: Exception =>
        {
          System.out.println("Universal Electricity Threaded Ticker: Failed while tcking updater. This is a bug! Clearing all tickers for self repair.")
          updaters.clear
          e.printStackTrace
        }
      }
  }
}