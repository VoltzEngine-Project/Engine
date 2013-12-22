package universalelectricity.core.net;

import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.api.net.INetwork;

/**
 * An grid-like, world cable-based network.
 * 
 * Inspired by Thermal Expansion.
 * 
 * @author Calclavia
 * 
 */
public abstract class Network<N, C, A> implements INetwork<N, C, A>
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

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.hashCode() + ", Handlers: " + this.handlerSet.size() + ", Connectors: " + this.connectorSet.size() + "]";
	}

	public boolean isFirstConnector(C connector)
	{
		if (connector == null)
		{
			return false;
		}

		return this.connectorSet.iterator().next().equals(connector);
	}
}
