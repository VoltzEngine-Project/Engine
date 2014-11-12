package resonant.lib.grid;

import resonant.api.grid.IGrid;
import resonant.api.grid.INodeGrid;
import resonant.api.grid.IUpdate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Collection of nodes patterened in a grid
 */
public class Grid<N> implements IGrid<N>
{
	final Class nodeClass;
	private final Set<N> nodes = new HashSet<N>();

	/**
	 * @param node - class of the node used by the grid
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
        Iterator<N> it = getNodes().iterator();
        while (it.hasNext())
        {
            N node = it.next();
            if(node instanceof INodeGrid)
                ((INodeGrid)node).setGrid(null);
            it.remove();
        }
	}

	/**
	 * Called to rebuild the grid node by node
	 */
	public void reconstruct()
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

	/**
	 * Rebuilds the node during a grid rebuild
	 */
	protected void reconstructNode(N node)
	{
        if(node instanceof INodeGrid)
            ((INodeGrid)node).setGrid(this);
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
        if(node instanceof INodeGrid)
            ((INodeGrid)node).setGrid(this);
	}

	/**
	 * Removes an object from the node list
	 */
	public void remove(N node)
	{
		nodes.remove(node);
        if(node instanceof INodeGrid)
            ((INodeGrid)node).setGrid(null);
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
		return (N) nodes.toArray()[0];
	}

    /**
     * Joins the two grids together as one grid
     * @param grid
     */
    public void merge(IGrid grid)
    {
        if(grid.getClass() == this.getClass())
        {
            IGrid mergedGrid = this;
            Set<N> nodes = grid.getNodes();
            if(grid.getNodes().size() > getNodes().size())
            {
                mergedGrid = grid;
                nodes = getNodes();
            }

            //Add all nodes to the other grid
            Iterator<N> it = nodes.iterator();
            while(it.hasNext())
            {
                N node = it.next();
                mergedGrid.add(node);
                if(mergedGrid.getNodes().contains(node))
                    it.remove();
            }

            // Most likely the grids should be empty, so we need to deconstruct if they are
            if(mergedGrid == this)
            {
                if(grid.getNodes().size() == 0)
                {
                    grid.deconstruct();
                }
            }
            else
            {
                if(mergedGrid.getNodes().size() == 0)
                {
                    mergedGrid.deconstruct();
                }
            }
        }
    }

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + hashCode() + ", Nodes: " + nodes.size() + "]";
	}
}
