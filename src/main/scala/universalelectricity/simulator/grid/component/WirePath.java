package universalelectricity.simulator.grid.component;

import universalelectricity.simulator.grid.SimulatedGrid;

import java.util.WeakHashMap;

/**
 * Wraps one or more nodes into a simple object that is used in the NetworkSimulation
 * @author Darkguardsman
 */
public class WirePath extends NetworkPart
{
    IComponent connectionA = null;
    IComponent connectionB = null;
    WeakHashMap<SimNode, Object> reference;

    public WirePath(SimulatedGrid sim, SimNode... nodes)
    {
        super(sim);
        reference = new WeakHashMap();
        for(SimNode node: nodes)
        {

        }
    }

    public void add(SimNode node)
    {
        reference.put(node, true);
    }

    public void remove(SimNode node)
    {
        reference.remove(node);
    }

    public void setConnectionA(IComponent part)
    {
        this.connectionB = part;
    }

    public void setConnectionB(IComponent part)
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
