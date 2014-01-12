package universalelectricity.core.net;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import universalelectricity.api.net.INetwork;
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

	private final LinkedHashSet<INetwork> toAddNetworks = new LinkedHashSet<INetwork>();
	private final LinkedHashSet<INetwork> networks = new LinkedHashSet<INetwork>();

	/**
	 * For queuing Forge events to be invoked the next tick.
	 */
	private final LinkedHashSet<Event> toAddEvents = new LinkedHashSet<Event>();
	private final LinkedHashSet<Event> queuedEvents = new LinkedHashSet<Event>();

	public static void addNetwork(INetwork network)
	{
		synchronized (INSTANCE.toAddNetworks)
		{
			if (!INSTANCE.networks.contains(network))
			{
				INSTANCE.toAddNetworks.add(network);
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

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		/**
		 * Network
		 */
		this.networks.addAll(this.toAddNetworks);
		this.toAddNetworks.clear();

		Iterator<INetwork> networkIt = this.networks.iterator();

		while (networkIt.hasNext())
		{
			INetwork network = networkIt.next();

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
		toAddNetworks.clear();
		networks.clear();
	}

}
