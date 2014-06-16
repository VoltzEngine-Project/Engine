package universalelectricity.core.grid

import cpw.mods.fml.common.eventhandler.{SubscribeEvent, Event}
import net.minecraftforge.common.MinecraftForge
import cpw.mods.fml.common.gameevent.TickEvent
import com.nicta.scoobi.impl.collection.WeakHashSet
import scala.collection.mutable

/**
 * A ticker to update all grids. This is multi-threaded based on configuration.
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
  private final var updaters = new WeakHashSet[IUpdate]()
  /**
   * For queuing Forge events to be invoked the next tick.
   */
  private final val queuedEvents = new mutable.SynchronizedQueue[Event]()

  /**
   * Becomes true if the network needs to be paused.
   */
  var pause = false

  var useThreads = true

  /**
   * The time in milliseconds between successive updates.
   */
  private var deltaTime = 0L

  def addUpdater(updater: IUpdate)
  {
    updaters synchronized
            {
              updaters += updater
            }
  }

  def queueEvent(event: Event)
  {
    queuedEvents synchronized
            {
              queuedEvents += event
            }
  }

  def getDeltaTime = deltaTime

  def getUpdaterCount = updaters.size

  override def run
  {
    var last = System.currentTimeMillis()

    while (true)
    {
      if (!pause)
      {
        val current = System.currentTimeMillis()
        deltaTime = current - last

        updaters synchronized
                {
                  update()
                }

        queuedEvents synchronized
                {
                  queuedEvents.foreach(MinecraftForge.EVENT_BUS.post(_))
                  queuedEvents.clear()
                }

        last = current
      }

      Thread.sleep(50L)
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
              queuedEvents.foreach(MinecraftForge.EVENT_BUS.post(_))
              queuedEvents.clear()
            }
  }

  def update()
  {
    try
    {
      updaters.filter(_.canUpdate()).foreach(_.update(getDeltaTime / 1000f))
      updaters = updaters.filterNot(_.continueUpdate()).asInstanceOf[WeakHashSet[IUpdate]]

      //TODO: Check if this works properly
    }
    catch
      {
        case e: Exception =>
        {
          System.out.println("Universal Electricity Ticker: Failed while ticking updaters. This is a bug! Clearing all tickers for self repair.")
          updaters.clear
          e.printStackTrace
        }
      }
  }
}