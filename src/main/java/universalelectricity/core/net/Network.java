package universalelectricity.core.net;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.api.net.IConnector;
import universalelectricity.api.net.INetwork;

/**
 * A network with only connectors and no handlers.
 * 
 * @author Calclavia
 * 
 * @param<N> - The grid/network type.
 * @param <C> - The connector type.
 */
public abstract class Network<N extends INetwork, C extends IConnector> implements INetwork<N, C>
{
	/**
	 * A set of connectors (e.g conductors).
	 */
	private final Set<C> connectors = new LinkedHashSet<C>();

	public abstract N newInstance();

	@Override
	public void addConnector(C connector)
	{
		this.connectors.add(connector);
	}

	@Override
	public void removeConnector(C connector)
	{
		this.connectors.remove(connector);
	}

	@Override
	public Set<C> getConnectors()
	{
		return this.connectors;
	}

	/**
	 * A simple reconstruct class. You may override this.
	 */
	public void reconstruct()
	{
		Iterator<C> it = this.getConnectors().iterator();

		while (it.hasNext())
		{
			C connector = it.next();

			if (this.isValidConnector(connector))
			{
				this.reconstructConnector(connector);
			}
			else
			{
				it.remove();
			}
		}
	}

	public boolean isValidConnector(C node)
	{
		return true;
	}

	/**
	 * Gets the first connector in the set.
	 * 
	 * @return
	 */
	public C getFirstConnector()
	{
		for (C node : this.getConnectors())
		{
			return node;
		}

		return null;
	}

	protected void reconstructConnector(C node)
	{

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
		if (network != null && network.getClass().isAssignableFrom(this.getClass()) && network != this)
		{
			N newNetwork = newInstance();
			newNetwork.getConnectors().addAll(this.getConnectors());
			newNetwork.getConnectors().addAll(network.getConnectors());

			network.getConnectors().clear();
			getConnectors().clear();

			newNetwork.reconstruct();
			return newNetwork;
		}

		return null;
	}

	@Override
	public void split(C splitPoint)
	{
		Class connectorClass = splitPoint.getClass();
		this.removeConnector(splitPoint);
		this.reconstruct();

		/**
		 * Loop through the connected blocks and attempt to see if there are connections between the
		 * two points elsewhere.
		 */
		Object[] connectedBlocks = splitPoint.getConnections();

		for (int i = 0; i < connectedBlocks.length; i++)
		{
			Object connectedBlockA = connectedBlocks[i];

			if (connectedBlockA != null && connectorClass.isAssignableFrom(connectedBlockA.getClass()))
			{
				for (int ii = 0; ii < connectedBlocks.length; ii++)
				{
					final Object connectedBlockB = connectedBlocks[ii];

					if (connectedBlockB != null && connectedBlockA != connectedBlockB && connectorClass.isAssignableFrom(connectedBlockB.getClass()))
					{
						ConnectionPathfinder<C> finder = new ConnectionPathfinder<C>((C) connectedBlockB, splitPoint);
						finder.findNodes((C) connectedBlockA);

						if (finder.results.size() <= 0)
						{
							try
							{
								/**
								 * The connections A and B are not connected anymore. Give them both
								 * a new common network.
								 */
								N newNetwork = newInstance();

								for (C node : finder.closedSet)
								{
									if (node != splitPoint)
									{
										newNetwork.addConnector(node);
										removeConnector(node);
										onSplit(newNetwork);
									}
								}
								newNetwork.reconstruct();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}

						}
					}
				}
			}
		}
	}

	@Override
	public void split(C connectorA, C connectorB)
	{
		this.reconstruct();

		if (connectorA != null && connectorB != null)
		{
			/** Check if connectorA connects with connectorB. */
			ConnectionPathfinder<C> finder = new ConnectionPathfinder<C>(connectorB);
			finder.findNodes(connectorA);

			if (finder.results.size() <= 0)
			{
				/**
				 * The connections A and B are not connected anymore. Give them both a new common
				 * network.
				 */
				N newNetwork = newInstance();

				for (C node : finder.closedSet)
				{
					newNetwork.addConnector(node);
				}

				newNetwork.reconstruct();
			}
		}
	}

	/**
	 * Called when the network successfully splits with another and iterates through all the split
	 * nodes.
	 * 
	 * @param newNetwork
	 */
	public void onSplit(N newNetwork)
	{
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.hashCode() + ", Connectors: " + this.connectors.size() + "]";
	}
}
