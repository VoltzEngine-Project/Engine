package resonant.lib.grid;

import resonant.api.grid.IGrid;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Collection of nodes patterened in a grid  */
public class Grid<N> implements IGrid<N>
{
    final Class nodeClass;
    private final Set<N> nodes = new HashSet<N>();

    /** @param node - class of the node used by the grid */
    public Grid(Class node)
    {
        this.nodeClass = node;
    }

    /** Destroys the grid and all of its data */
    public void deconstruct()
    {
        nodes.clear();
    }

    /** Called to rebuild the grid node by node */
    public void reconstruct()
    {
        Iterator<N> it = getNodes().iterator();
        while(it.hasNext())
        {
            N node = it.next();
            if(isValidNode(node))
            {
                reconstructNode(node);
            }else
            {
                nodes.remove(node);
            }
        }
    }

    /** Rebuilds the node during a grid rebuild */
    protected void reconstructNode(N node)
    {
    }

    /** Checks to see if the node is valid */
    public boolean isValidNode(Object node)
    {
        //TODO: Check if better way to do this.
        return nodeClass.isAssignableFrom(node.getClass());
    }

    /** Adds an object to the node list */
    public void add(N node)
    {
        nodes.add(node);
    }

    /** Removes an object from the node list */
    public void remove(N node)
    {
        nodes.remove(node);
    }

    /** Gets the list of all nodes */
    public Set<N> getNodes()
    {
        return nodes;
    }

    /** Gets the first node in the list */
    public N getFirstNode()
    {
        return (N)nodes.toArray()[0];
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + hashCode() + ", Nodes: " + nodes.size() + "]";
    }
}
