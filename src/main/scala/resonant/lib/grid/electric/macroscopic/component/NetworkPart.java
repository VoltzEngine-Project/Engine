package resonant.lib.grid.electric.macroscopic.component;

import universalelectricity.api.core.grid.sim.ISimNode;
import universalelectricity.core.transform.vector.VectorWorld;
import resonant.lib.grid.electric.macroscopic.SimulatedGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dark
 */
public class NetworkPart implements IComponent
{
	private SimulatedGrid sim;
    private List<IComponent> connections;
    private List<ISimNode> nodes;

	public NetworkPart(SimulatedGrid sim, ISimNode... nodes)
	{
		this.sim = sim;
        connections = new ArrayList<IComponent>();
        this.nodes = new ArrayList<ISimNode>();
        for(ISimNode node : nodes)
        {
            this.nodes.add(node);
        }
	}

    @Override
    public SimulatedGrid getSimulator()
    {
        return sim;
    }

    @Override
    public List<IComponent> connections()
    {
        return connections;
    }

    @Override
    public boolean doesExistAt(VectorWorld vec)
    {
        for(ISimNode node : nodes)
        {
            if(vec.equals(node))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getPressureChange(IComponent from, IComponent to)
    {
        if(connections.contains(from) && connections.contains(to))
        {
            int c = connections.size() - 1;
            if(c > 0)
            {
                return 1.0 / c;
            }

        }
        return 1.0;
    }

    @Override
    public double getFlowChange(IComponent from, IComponent to)
    {
        if(connections.contains(from) && connections.contains(to))
        {
            int c = connections.size() - 1;
            if(c > 0)
            {
                return 1 / c;
            }
        }
        return 1;
    }

    @Override
    public void destroy()
    {
        sim = null;
        connections = null;
        nodes = null;
    }

    @Override
    public void build()
    {

    }
}
