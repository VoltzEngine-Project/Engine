package universalelectricity.api.net;

import java.util.Set;

/**
 * Implement this in your network class/interface if you plan to have your own network defined by
 * specific conductors and acceptors.
 * 
 * @author Calclavia, Aidancbrady
 * 
 * @param <N> - the class/interface Type value in which you implement this
 * @param <C> - the class/interface Type which makes up the network's connector set
 * @param <A> - the class/interface Type which makes up the network's node set
 */
public interface INetwork<N extends INetwork, C extends IConnector, A> extends IGrid<N, A>, IUpdate
{
	public void addConnector(C connector);

	public void removeConnector(C connector);

	/**
	 * Gets the set of conductors that make up this network.
	 * 
	 * @return conductor set
	 */
	public Set<C> getConnectors();

	public void reconstruct();

	/**
	 * Splits a network by removing a conductor referenced in the parameter. It will then create and
	 * refresh the new independent networks possibly created by this operation.
	 * 
	 * @param connection
	 */
	public void split(C connection);

	/**
	 * Splits the network between 2 connectors, separating their networks.
	 * 
	 * @param connectorA
	 * @param connectorB
	 */
	public void split(C connectorA, C connectorB);

}
