package universalelectricity.core.net;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import universalelectricity.api.net.IUpdate;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A ticker to update all networks. Register your custom network here to have it ticked by Universal
 * Electricity.
 * 
 * @author Calclavia
 */
public class NetworkTickHandler implements ITickHandler
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

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
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

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel()
	{
		return "Universal Electricity Ticker";
	}
}
