package universalelectricity.core.net;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import universalelectricity.api.net.INetwork;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/** A ticker to update all networks. Register your custom network here to have it ticked by Universal
 * Electricity.
 * 
 * @author Calclavia */
public class NetworkTickHandler implements ITickHandler
{
    public static final NetworkTickHandler INSTANCE = new NetworkTickHandler();

    private final LinkedHashSet<INetwork> toAddNetworks = new LinkedHashSet<INetwork>();
    private final LinkedHashSet<INetwork> networks = new LinkedHashSet<INetwork>();

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

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {

    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {

        this.networks.addAll(this.toAddNetworks);
        this.toAddNetworks.clear();

        Iterator<INetwork> it = this.networks.iterator();

        while (it.hasNext())
        {
            INetwork network = it.next();

            if (network.canUpdate())
            {
                network.update();
            }

            if (!network.continueUpdate())
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
