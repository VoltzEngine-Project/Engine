package universalelectricity.core.net;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.api.net.IConnector;

/**
 * A connector-less network.
 * 
 * @author Calclavia
 * 
 * @param<N> - The grid/network type
 * @param <A> - The handler type of the nodes that will be connected
 */
public abstract class Grid<N, C, A>
{
	/**
	 * A set of handlers that handles events from the network.
	 */
	private final Set<A> handlerSet = new LinkedHashSet<A>();

	public void add(A battery)
	{
		this.handlerSet.add(battery);
	}

	public void remove(A battery)
	{
		this.handlerSet.remove(battery);
	}

	public Set<A> getNodes()
	{
		return this.handlerSet;
	}

	public void refresh()
	{
		Iterator<A> it = this.handlerSet.iterator();

		while (it.hasNext())
		{
			A node = it.next();

			if (this.isValidNode(node))
			{
				this.reconstructNode(node);
			}
			else
			{
				it.remove();
			}
		}
	}

	public N merge(N network)
	{
		if (network != this)
		{
			N newNetwork = newInstance();
			newNetwork.get().addAll(this.get());
			newNetwork.get().addAll(network.get());
			newNetwork.refresh();

			this.handlerSet.clear();
		}
	}

	public void split(IConnector splitNode)
	{
		this.remove((A) splitNode);

		/**
		 * Loop through the connected blocks and attempt to see if there are connections between the
		 * two points elsewhere.
		 */
		Object[] connectedBlocks = splitNode.getConnections();

		for (int i = 0; i < connectedBlocks.length; i++)
		{
			Object connectedBlockA = connectedBlocks[i];

			if (connectedBlockA instanceof IConnector)
			{
				for (int ii = 0; ii < connectedBlocks.length; ii++)
				{
					final Object connectedBlockB = connectedBlocks[ii];

					if (connectedBlockA != connectedBlockB && connectedBlockB instanceof IConnector)
					{

						ConnectionPathfinder finder = new ConnectionPathfinder((IConnector) connectedBlockB, splitNode);
						finder.findNodes((IConnector) connectedBlockA);

						if (finder.results.size() <= 0)
						{
							/**
							 * The connections A and B are not connected anymore. Give them both
							 * a new common network.
							 */
							Structure newStructure = this.getNew();

							for (IConnector node : finder.closedSet)
							{
								if (node != splitNode)
								{
									newStructure.add((A) node);
									this.remove((A) node);
								}
							}

							newStructure.refresh();
						}
					}
				}
			}
		}
	}

	public boolean isValidNode(A node)
	{
		return node instanceof IConnector;
	}

	public A getFirstNode()
	{
		for (A node : this.get())
		{
			return node;
		}

		return null;
	}

	public abstract N newInstance();

	protected abstract void reconstructNode(A node);
}
