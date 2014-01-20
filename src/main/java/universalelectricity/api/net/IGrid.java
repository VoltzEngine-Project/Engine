package universalelectricity.api.net;

import java.util.Set;

public interface IGrid<N extends INetwork, A>
{

	/**
	 * * @return The list of nodes in the network.
	 */
	public Set<A> getNodes();

	/**
	 * Creates a new network that makes up the current network and the network defined in the
	 * parameters. Be sure to refresh the new network inside this method.
	 * 
	 * @param network - network to merge
	 * @return The new network instance.
	 */
	public N merge(N network);
}
