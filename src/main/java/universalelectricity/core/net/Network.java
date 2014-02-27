package universalelectricity.core.net;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

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
	private final Set<C> connectors = Collections.newSetFromMap(new WeakHashMap<C, Boolean>());

	public abstract N newInstance();

	@Override
	public void addConnector(C connector)
	{
		connectors.add(connector);
	}

	@Override
	public void removeConnector(C connector)
	{
		connectors.remove(connector);
	}

	@Override
	public Set<C> getConnectors()
	{
		return connectors;
	}

	/**
	 * A simple reconstruct class. You may override
	 */
	@Override
	public void reconstruct()
	{
		Iterator<C> it = new HashSet<C>(getConnectors()).iterator();

		while (it.hasNext())
		{
			C connector = it.next();

			if (isValidConnector(connector))
			{
				reconstructConnector(connector);
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
		for (C node : getConnectors())
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
		if (network != null && network.getClass().isAssignableFrom(getClass()) && network != this)
		{
			synchronized (getConnectors())
			{
				N newNetwork = newInstance();
				newNetwork.getConnectors().addAll(getConnectors());
				newNetwork.getConnectors().addAll(network.getConnectors());

				network.getConnectors().clear();
				getConnectors().clear();

				newNetwork.reconstruct();
				return newNetwork;
			}
		}

		return null;
	}

	@Override
	public void split(C splitPoint)
	{
		Class connectorClass = splitPoint.getClass();
		removeConnector(splitPoint);
		reconstruct();

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
						ConnectionPathfinder<C> finder = new ConnectionPathfinder<C>(getConnectorClass(), (C) connectedBlockB, splitPoint);
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
		reconstruct();

		if (connectorA != null && connectorB != null)
		{
			/** Check if connectorA connects with connectorB. */
			ConnectionPathfinder<C> finder = new ConnectionPathfinder<C>(getConnectorClass(), connectorB);
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

	public Class getConnectorClass()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + hashCode() + ", Connectors: " + connectors.size() + "]";
	}
}
