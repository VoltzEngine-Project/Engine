package calclavia.lib.multiblock.structure;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.api.net.IConnector;
import universalelectricity.core.net.ConnectionPathfinder;

/**
 * A Multiblock Structure Network
 * 
 * @author Calclavia
 * 
 */
public abstract class Structure<N>
{
	private Set<N> nodes = new LinkedHashSet<N>();

	public void add(N battery)
	{
		this.nodes.add(battery);
	}

	public void remove(N battery)
	{
		this.nodes.remove(battery);
	}

	public Set<N> get()
	{
		return this.nodes;
	}

	public void refresh()
	{
		Iterator<N> it = this.nodes.iterator();

		while (it.hasNext())
		{
			N node = it.next();

			if (this.isValidNode(node))
			{
				this.refreshNode(node);
			}
			else
			{
				it.remove();
			}
		}
	}

	public void merge(Structure structure)
	{
		if (structure != this)
		{
			Structure newStructure = this.getNew();
			newStructure.get().addAll(this.get());
			newStructure.get().addAll(structure.get());
			newStructure.refresh();

			this.nodes.clear();
		}
	}

	public void split(IConnector splitNode)
	{
		this.remove((N) splitNode);

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
									newStructure.add((N) node);
									this.remove((N) node);
								}
							}

							newStructure.refresh();
						}
					}
				}
			}
		}
	}

	public boolean isValidNode(N node)
	{
		return node instanceof IConnector;
	}

	public N getFirstNode()
	{
		for (N node : this.get())
		{
			return node;
		}

		return null;
	}

	public abstract Structure getNew();

	protected abstract void refreshNode(N node);
}
