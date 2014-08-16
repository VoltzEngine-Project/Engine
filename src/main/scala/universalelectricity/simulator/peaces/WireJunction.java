package universalelectricity.simulator.peaces;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.simulator.NetworkSimulator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robert on 8/16/2014.
 */
public class WireJunction extends NetworkPart
{
    HashMap<Object, ForgeDirection> connectionMap;
    HashMap<ForgeDirection, Boolean> inputMap;

    public WireJunction(NetworkSimulator sim)
    {
        super(sim);
    }

    public void add(Object object, ForgeDirection side, boolean input)
    {
        connectionMap.put(object, side);
        inputMap.put(side, input);
    }

    public void remove(Object object)
    {
        if(connectionMap.containsKey(object))
        {
            ForgeDirection direction = connectionMap.get(object);
            connectionMap.remove(object);
            inputMap.remove(direction);
        }
    }

    @Override
    public boolean hasInputDevices()
    {
        for(Object object : connectionMap.keySet())
        {
            if(object instanceof INetworkPart && ((INetworkPart) object).hasInputDevices())
            {
                return true;
            }else
            {
                return sim.isInputDevice(object, connectionMap.get(object));
            }
        }
        return false;
    }

}
