package universalelectricity.simulator.grid.parts;

import universalelectricity.simulator.grid.LinkedGrid;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkPart implements INetworkPart {

    LinkedGrid sim;
    public NetworkPart(LinkedGrid sim)
    {
        this.sim = sim;
    }
    @Override
    public boolean hasInputDevices()
    {
        return false;
    }

    @Override
    public boolean hasOutputDevices()
    {
        return false;
    }
}
