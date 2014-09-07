package universalelectricity.simulator.parts;

import universalelectricity.simulator.SimulatedGrid;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkPart implements INetworkPart {

    SimulatedGrid sim;
    public NetworkPart(SimulatedGrid sim)
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
