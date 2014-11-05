package resonant.lib.grid.electric.macroscopic.part;

import resonant.api.grid.sim.IPathNode;
import resonant.lib.grid.electric.macroscopic.PathGrid;
import resonant.lib.grid.electric.macroscopic.node.IComponent;

import java.util.WeakHashMap;

/**
 * Wraps one or more nodes into a simple object that is used in the NetworkSimulation
 *
 * @author Darkguardsman
 */
public class Bundle extends GridPart
{
	IComponent connectionA = null;
	IComponent connectionB = null;
	WeakHashMap<IPathNode, Object> reference;

	public Bundle(PathGrid sim, IPathNode... nodes)
	{
		super(sim);
		reference = new WeakHashMap();

		for (IPathNode node : nodes)
		{

		}
	}

	public void add(IPathNode node)
	{
		reference.put(node, true);
	}

	public void remove(IPathNode node)
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

}
