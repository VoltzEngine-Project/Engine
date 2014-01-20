package universalelectricity.core.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import universalelectricity.api.net.IConnector;

/**
 * Check if a connector connects with another in a grid of connectors. Note: This does NOT find the
 * shortest path.
 * 
 * @author Calclavia
 */
public class ConnectionPathfinder<C extends IConnector>
{
	/** A list of nodes that the pathfinder already went through. */
	public final Set<C> closedSet = new LinkedHashSet<C>();

	/** The resulted path found by the pathfinder. Could be null if no path was found. */
	public final Set<C> results = new LinkedHashSet<C>();

	protected final Class<? extends IConnector> connectorClass;

	protected final C targetConnector;
	protected final List<C> ignoreConnector;

	public ConnectionPathfinder(C targetConnector, C... ignoreConnector)
	{
		this.targetConnector = targetConnector;
		connectorClass = this.targetConnector.getClass();

		if (ignoreConnector != null)
		{
			this.ignoreConnector = Arrays.asList(ignoreConnector);
		}
		else
		{
			this.ignoreConnector = new ArrayList<C>();
		}
	}

	/**
	 * A recursive function to find all connectors.
	 * 
	 * @return True on success finding, false on failure.
	 */
	public boolean findNodes(C currentNode)
	{
		this.closedSet.add(currentNode);

		if (this.onSearch(currentNode))
		{
			return false;
		}

		for (C node : this.getConnectedNodes(currentNode))
		{
			if (!this.closedSet.contains(node))
			{
				if (this.findNodes(node))
				{
					return true;
				}
			}
		}

		return false;
	}

	public Set<C> getConnectedNodes(C currentNode)
	{
		Set<C> connectedNodes = new HashSet<C>();

		if (currentNode != null)
		{
			if (currentNode.getConnections() != null)
			{
				for (int i = 0; i < currentNode.getConnections().length; i++)
				{
					Object obj = currentNode.getConnections()[i];

					if (obj != null && connectorClass.isAssignableFrom(obj.getClass()) && !this.ignoreConnector.contains(obj))
					{
						connectedNodes.add((C) obj);
					}
				}
			}
		}

		return connectedNodes;
	}

	public boolean onSearch(C node)
	{
		if (node == this.targetConnector)
		{
			this.results.add(node);
			return true;
		}

		return false;
	}

	public void reset()
	{
		this.results.clear();
		this.closedSet.clear();
	}

}
