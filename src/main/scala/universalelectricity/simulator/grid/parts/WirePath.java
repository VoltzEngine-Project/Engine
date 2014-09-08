package universalelectricity.simulator.grid.parts;

import universalelectricity.simulator.grid.LinkedGrid;

import java.util.WeakHashMap;

/**
 * Wraps one or more nodes into a simple object that is used in the NetworkSimulation
 * @author Darkguardsman
 */
public class WirePath extends NetworkPart
{
    INetworkPart connectionA = null;
    INetworkPart connectionB = null;
    WeakHashMap<NetworkNode, Object> reference;

    public WirePath(LinkedGrid sim, NetworkNode... nodes)
    {
        super(sim);
        reference = new WeakHashMap();
        for(NetworkNode node: nodes)
        {

        }
    }

    public void add(NetworkNode node)
    {
        reference.put(node, true);
    }

    public void remove(NetworkNode node)
    {
        reference.remove(node);
    }

    public void setConnectionA(INetworkPart part)
    {
        this.connectionB = part;
    }

    public void setConnectionB(INetworkPart part)
    {
        this.connectionA = part;
    }

    public boolean isConnected()
    {
        return connectionA != null && connectionB != null;
    }

    public boolean hasInputsDevices()
    {
        return connectionA.hasInputDevices() || connectionB.hasInputDevices();
    }

}
