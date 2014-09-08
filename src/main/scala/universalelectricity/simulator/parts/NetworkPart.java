package universalelectricity.simulator.parts;

import universalelectricity.simulator.grid.SimulationGrid;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkPart implements INetworkPart {

    SimulationGrid sim;
    public NetworkPart(SimulationGrid sim)
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
