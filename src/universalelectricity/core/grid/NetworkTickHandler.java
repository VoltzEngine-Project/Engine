package universalelectricity.core.grid;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import universalelectricity.api.INetwork;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * @author Calclavia
 * 
 */
public class NetworkTickHandler implements ITickHandler
{
	public static final NetworkTickHandler INSTANCE = new NetworkTickHandler();

	public static final LinkedHashSet<INetwork> toAddNetworks = new LinkedHashSet<INetwork>();
	public static final LinkedHashSet<INetwork> networks = new LinkedHashSet<INetwork>();

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		this.networks.addAll(this.toAddNetworks);

		Iterator<INetwork> it = this.networks.iterator();

		while (it.hasNext())
		{
			INetwork network = it.next();

			if (network.getConnectors().size() > 0)
			{
				network.update();
			}
			else
			{
				it.remove();
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
		return "Universal Electricity Grid Ticker";
	}

}
