package universalelectricity.simulator.peaces;

import universalelectricity.simulator.NetworkSimulator;

import java.util.WeakHashMap;

/**
 * Wraps one or more nodes into a simple object that is used in the NetworkSimulation
 * @author Darkguardsman
 */
public class WirePath extends NetworkPart
{
    INetworkPart inputConnection = null;
    INetworkPart outputConnection = null;
    WeakHashMap<NetworkWire, Object> reference;

    public WirePath(NetworkSimulator sim, NetworkWire... nodes)
    {
        super(sim);
        reference = new WeakHashMap<>();
        for(NetworkNode node: nodes)
        {

        }
    }

    public void add(NetworkWire node)
    {
        reference.put(node, true);
    }

    public void remove(NetworkWire node)
    {
        reference.remove(node);
    }

    public void setOutput(INetworkPart part)
    {
        this.outputConnection = part;
    }

    public void setInput(INetworkPart part)
    {
        this.inputConnection = part;
    }

    public boolean isConnected()
    {
        return inputConnection != null && outputConnection != null;
    }

    public boolean hasInputsDevices()
    {
        return inputConnection.hasInputDevices();
    }

}
