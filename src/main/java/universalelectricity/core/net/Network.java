package universalelectricity.core.net;

import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.api.energy.EnergyNetworkLoader;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.energy.IEnergyNetwork;
import universalelectricity.api.net.IConnector;
import universalelectricity.api.net.INetwork;

/**
 * An grid-like, world cable-based network.
 * 
 * Inspired by Thermal Expansion.
 * 
 * @author Calclavia
 * 
 */
public abstract class Network<N extends INetwork, C extends IConnector, A> implements INetwork<N, C, A>
{
	/**
	 * A set of handlers that handles events from the network.
	 */
	private final Set<A> handlerSet = new LinkedHashSet<A>();

	/**
	 * A set of connectors (e.g conductors).
	 */
	private final Set<C> connectorSet = new LinkedHashSet<C>();

	@Override
	public void addConnector(C connector)
	{
		this.connectorSet.add(connector);
	}

	@Override
	public void removeConnector(C connector)
	{
		this.connectorSet.remove(connector);
	}

	@Override
	public Set<C> getConnectors()
	{
		return this.connectorSet;
	}

	@Override
	public Set<A> getNodes()
	{
		return this.handlerSet;
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
		if (this.getClass().isAssignableFrom(network.getClass()) && network != this)
		{
			N newNetwork = newInstance();
			newNetwork.getConnectors().addAll(this.getConnectors());
			newNetwork.getConnectors().addAll(network.getConnectors());

			network.getConnectors().clear();
			network.getNodes().clear();
			getConnectors().clear();
			getNodes().clear();

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

			if (connectorClass.isAssignableFrom(connectedBlockA.getClass()))
			{
				for (int ii = 0; ii < connectedBlocks.length; ii++)
				{
					final Object connectedBlockB = connectedBlocks[ii];

					if (connectedBlockA != connectedBlockB && connectorClass.isAssignableFrom(connectedBlockB.getClass()))
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
	public void split(C connectorA, C connectorB)
	{
		this.reconstruct();

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

	public abstract N newInstance();

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.hashCode() + ", Handlers: " + this.handlerSet.size() + ", Connectors: " + this.connectorSet.size() + "]";
	}
}
