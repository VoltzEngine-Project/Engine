package resonant.lib.grid;

import resonant.api.grid.IGrid;
import resonant.api.grid.INode;
import resonant.api.grid.INodeGrid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Collection of nodes patterened in a grid
 */
public class Grid<N> implements IGrid<N>
{
	final Class nodeClass;
	private final Set<N> nodes = Collections.newSetFromMap(new HashMap<N, Boolean>());

	/**
	 * @param node - class of the node used by the grid, use for checking if nodes added are valid
	 */
	public Grid(Class node)
	{
		this.nodeClass = node;
	}

	/**
	 * Destroys the grid and all of its data
	 */
	public void deconstruct()
	{
		synchronized (nodes)
		{
			//Kill the nodes connection to the grid
			for (N node : getNodes())
			{
				if (node instanceof INodeGrid)
					((INodeGrid) node).setGrid(null);
				if (node instanceof INode)
					((INode) node).deconstruct();
			}
			//empty the node list
			getNodes().clear();
		}
	}

	/**
	 * Called to rebuild the grid node by node
	 */
	public void reconstruct()
	{
		synchronized (nodes)
		{
			Iterator<N> it = getNodes().iterator();
			while (it.hasNext())
			{
				N node = it.next();
				if (isValidNode(node))
				{
					reconstructNode(node);
				}
				else
				{
					nodes.remove(node);
				}
			}
		}
	}

	/**
	 * Rebuilds the node during a grid rebuild
	 */
	protected void reconstructNode(N node)
	{
		if (node instanceof INodeGrid)
			((INodeGrid) node).setGrid(this);
	}

	/**
	 * Checks to see if the node is valid
	 */
	public boolean isValidNode(Object node)
	{
		return node != null && nodeClass.isAssignableFrom(node.getClass());
	}

	/**
	 * Adds an object to the node list
	 */
	public void add(N node)
	{
		nodes.add(node);
		if (node instanceof INodeGrid)
			((INodeGrid) node).setGrid(this);
	}

	/**
	 * Removes a node from the node list.
	 * <p/>
	 * Do not do any reconstruct when called as
	 * this is meant to clear the node from the grid
	 * only and can in fact be related to the node
	 * being added to another grid.
	 */
	public void remove(N node)
	{
		nodes.remove(node);
		if (node instanceof INodeGrid)
			((INodeGrid) node).setGrid(null);
	}

	/**
	 * Gets the list of all nodes
	 */
	public Set<N> getNodes()
	{
		return nodes;
	}

	/**
	 * Gets the first node in the list
	 */
	public N getFirstNode()
	{
		return nodes != null && !nodes.isEmpty() ? (N) nodes.toArray()[0] : null;
	}

	/**
	 * Joins the two grids together as one grid.
	 * Picks one grid then dumps all data into that grid
	 *
	 * @param grid - grid instance to join
	 */
	public void merge(IGrid grid)
	{
		if (grid != this && grid.getClass() == this.getClass())
		{
			IGrid mergedGrid = this;
			Set<N> nodes = grid.getNodes();
			if (grid.getNodes().size() > getNodes().size())
			{
				mergedGrid = grid;
				nodes = getNodes();
			}

			//Add all nodes to the other grid
			Iterator<N> it = nodes.iterator();
			while (it.hasNext())
			{
				N node = it.next();
				mergedGrid.add(node);
				if (mergedGrid.getNodes().contains(node))
					it.remove();
			}

			// Most likely the grids should be empty, so we need to deconstruct if they are
			if (mergedGrid == this)
			{
				if (grid.getNodes().size() == 0)
				{
					grid.deconstruct();
				}
			}
			else
			{
				if (mergedGrid.getNodes().size() == 0)
				{
					mergedGrid.deconstruct();
				}
			}
		}
	}

	/**
	 * Called to split the network into two or more smaller networks
	 *
	 * @param splitNode - node that was removed to cause the split
	 */
	public void split(N splitNode)
	{
		//TODO code something more effective
		deconstruct();
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + hashCode() + ", Nodes: " + nodes.size() + "]";
	}
}
