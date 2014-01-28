package universalelectricity.core.net;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

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

	private final LinkedHashSet<IUpdate> toAddUpdaters = new LinkedHashSet<IUpdate>();
	private final LinkedHashSet<IUpdate> updaters = new LinkedHashSet<IUpdate>();

	/**
	 * For queuing Forge events to be invoked the next tick.
	 */
	private final LinkedHashSet<Event> toAddEvents = new LinkedHashSet<Event>();
	private final LinkedHashSet<Event> queuedEvents = new LinkedHashSet<Event>();

	private boolean markClear = true;

	public static void addNetwork(IUpdate updater)
	{
		synchronized (INSTANCE.toAddUpdaters)
		{
			if (!INSTANCE.updaters.contains(updater))
			{
				INSTANCE.toAddUpdaters.add(updater);
			}
		}
	}

	public static void queueEvent(Event event)
	{
		synchronized (INSTANCE.toAddEvents)
		{
			if (!INSTANCE.queuedEvents.contains(event))
			{
				INSTANCE.toAddEvents.add(event);
			}
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (markClear)
		{
			toAddUpdaters.clear();
			updaters.clear();
			toAddEvents.clear();
			queuedEvents.clear();
			markClear = false;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		/**
		 * Network
		 */
		this.updaters.addAll(new HashSet<IUpdate>(this.toAddUpdaters));
		this.toAddUpdaters.clear();

		Iterator<IUpdate> networkIt = this.updaters.iterator();

		while (networkIt.hasNext())
		{
			IUpdate network = networkIt.next();

			if (network.canUpdate())
			{
				network.update();
			}

			if (!network.continueUpdate())
			{
				networkIt.remove();
			}
		}

		/**
		 * Events
		 */
		this.queuedEvents.addAll(this.toAddEvents);
		this.toAddEvents.clear();

		Iterator<Event> eventIt = this.queuedEvents.iterator();

		while (eventIt.hasNext())
		{
			MinecraftForge.EVENT_BUS.post(eventIt.next());
			eventIt.remove();
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
		return "Universal Electricity Grid Ticker";
	}

	public void clearNetworks()
	{
		markClear = true;
	}

}
