package universalelectricity.core.net;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import universalelectricity.api.net.IUpdate;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

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
	private final LinkedHashSet<Event> queuedEvents = new LinkedHashSet<Event>();

	private boolean markClear;

	public static void addNetwork(IUpdate updater)
	{
		synchronized (INSTANCE.updaters)
		{
			INSTANCE.updaters.add(updater);
		}
	}

	public static void queueEvent(Event event)
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

			Iterator<IUpdate> updaterIt = new HashSet(updaters).iterator();

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
				System.out.println(getLabel() + ": Failed while tcking updater. This is a bug! Clearing list for self repair.");
				updaters.clear();
				e.printStackTrace();
			}

			if (markClear)
			{
				updaters.clear();
			}
		}

		/** Perform all queued events */
		synchronized (queuedEvents)
		{
			Iterator<Event> eventIt = this.queuedEvents.iterator();

			while (eventIt.hasNext())
			{
				MinecraftForge.EVENT_BUS.post(eventIt.next());
				eventIt.remove();
			}

			if (markClear)
			{
				queuedEvents.clear();
			}
		}

		markClear = false;
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

	public void clearNetworks()
	{
		markClear = true;
	}
}
