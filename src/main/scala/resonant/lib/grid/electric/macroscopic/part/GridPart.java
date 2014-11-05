package resonant.lib.grid.electric.macroscopic.part;

import resonant.api.grid.sim.IPathNode;
import resonant.lib.grid.electric.macroscopic.PathGrid;
import resonant.lib.grid.electric.macroscopic.node.IComponent;
import resonant.lib.transform.vector.VectorWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dark
 */
public class GridPart implements IComponent
{
	private PathGrid sim;
	private List<IComponent> connections;
	private List<IPathNode> nodes;

	public GridPart(PathGrid sim, IPathNode... nodes)
	{
		this.sim = sim;
		connections = new ArrayList<IComponent>();
		this.nodes = new ArrayList<IPathNode>();
		for (IPathNode node : nodes)
		{
			this.nodes.add(node);
		}
	}

	@Override
	public PathGrid getSimulator()
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
		for (IPathNode node : nodes)
		{
			if (vec.equals(node))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public double getPressureChange(IComponent from, IComponent to)
	{
		if (connections.contains(from) && connections.contains(to))
		{
			int c = connections.size() - 1;
			if (c > 0)
			{
				return 1.0 / c;
			}

		}
		return 1.0;
	}

	@Override
	public double getFlowChange(IComponent from, IComponent to)
	{
		if (connections.contains(from) && connections.contains(to))
		{
			int c = connections.size() - 1;
			if (c > 0)
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
