package universalelectricity.core.net;

import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.api.net.IConnector;
import universalelectricity.api.net.INodeNetwork;

/**
 * An grid-like, world cable-based network.
 * 
 * Inspired by Thermal Expansion.
 * 
 * @author Calclavia
 * 
 */
public abstract class NodeNetwork<N extends INodeNetwork, C extends IConnector, A> extends Network<N, C> implements INodeNetwork<N, C, A>
{
	/**
	 * A set of handlers that handles events from the network.
	 */
	private final Set<A> nodes = new LinkedHashSet<A>();

	public void add(A battery)
	{
		this.nodes.add(battery);
	}

	public void remove(A battery)
	{
		this.nodes.remove(battery);
	}

	@Override
	public Set<A> getNodes()
	{
		return this.nodes;
	}

	/**
	 * Merges two networks together.
	 * 
	 * @param network - The other network to merge with.
	 * @return The new network created by the merge.
	 */
	@Override
	public N merge(N network)
	{
		N newNetwork = super.merge(network);

		if (newNetwork != null)
		{
			// Clear all nodes.
			network.getNodes().clear();
			getNodes().clear();
			return newNetwork;
		}

		return null;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.hashCode() + ", Nodes: " + this.nodes.size() + ", Connectors: " + this.getConnectors().size() + "]";
	}
}
