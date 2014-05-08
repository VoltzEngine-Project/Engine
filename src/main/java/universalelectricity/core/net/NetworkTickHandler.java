package universalelectricity.core.net;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.net.IUpdate;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * A ticker to update all networks. Register your custom network here to have it ticked by Universal
 * Electricity.
 * 
 * @author Calclavia
 */
public class NetworkTickHandler
{
	public static final NetworkTickHandler INSTANCE = new NetworkTickHandler();

	/** For updaters to be ticked. */
	private final Set<IUpdate> updaters = Collections.newSetFromMap(new WeakHashMap<IUpdate, Boolean>());

	/** For queuing Forge events to be invoked the next tick. */
	private final Queue<Event> queuedEvents = new ConcurrentLinkedQueue<Event>();

	public static void addNetwork(IUpdate updater)
	{
		synchronized (INSTANCE.updaters)
		{
			INSTANCE.updaters.add(updater);
		}
	}

	public static synchronized void queueEvent(Event event)
	{
		synchronized (INSTANCE.queuedEvents)
		{
			INSTANCE.queuedEvents.add(event);
		}
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event)
	{
		/** Tick all updaters. */
		synchronized (updaters)
		{
			Set<IUpdate> removeUpdaters = Collections.newSetFromMap(new WeakHashMap<IUpdate, Boolean>());

			Iterator<IUpdate> updaterIt = new HashSet<IUpdate>(updaters).iterator();

			try
			{
				while (updaterIt.hasNext())
				{
					IUpdate updater = updaterIt.next();

					if (updater.canUpdate())
					{
						updater.update();
					}

					if (!updater.continueUpdate())
					{
						removeUpdaters.add(updater);
					}
				}

				updaters.removeAll(removeUpdaters);
			}
			catch (Exception e)
			{
				System.out.println(getLabel() + ": Failed while tcking updater. This is a bug! Clearing all tickers for self repair.");
				updaters.clear();
				e.printStackTrace();
			}
		}

		/** Perform all queued events */
		synchronized (queuedEvents)
		{
			while (!queuedEvents.isEmpty())
			{
				MinecraftForge.EVENT_BUS.post(queuedEvents.poll());
			}
		}
	}

	public String getLabel()
	{
		return "Universal Electricity Ticker";
	}
}
