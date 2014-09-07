package universalelectricity.simulator.parts;

import universalelectricity.simulator.NetworkSimulator;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkPart implements INetworkPart {

    NetworkSimulator sim;
    public NetworkPart(NetworkSimulator sim)
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
