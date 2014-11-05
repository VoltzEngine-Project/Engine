package resonant.lib.grid.electric.macroscopic.component;

import resonant.api.grid.sim.ISimNode;
import resonant.lib.grid.electric.macroscopic.PathGrid;

import java.util.WeakHashMap;

/**
 * Wraps one or more nodes into a simple object that is used in the NetworkSimulation
 *
 * @author Darkguardsman
 */
public class WirePath extends NetworkPart
{
	IComponent connectionA = null;
	IComponent connectionB = null;
	WeakHashMap<ISimNode, Object> reference;

	public WirePath(PathGrid sim, ISimNode... nodes)
	{
		super(sim);
		reference = new WeakHashMap();

		for (ISimNode node : nodes)
		{

		}
	}

	public void add(ISimNode node)
	{
		reference.put(node, true);
	}

	public void remove(ISimNode node)
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
