package resonant.lib.grid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

/** A grid specifying a connection with a series of nodes.
 * 
 * @author Calclavia
 * 
 * @param <N> - The node type. */
public abstract class Grid<N>
{
    /** A set of connectors (e.g conductors). */
    protected final Set<N> nodes = Collections.newSetFromMap(new WeakHashMap<N, Boolean>());
    private final Class<? extends N> nodeType;

    public Grid(Class<? extends N> type)
    {
        nodeType = type;
    }

    public void add(N node)
    {
        synchronized (nodes)
        {
            nodes.add(node);
        }
    }

    public void remove(N node)
    {
        synchronized (nodes)
        {
            nodes.remove(node);
        }
    }

    /** This is READ ONLY! Make sure nodes are synchronized if modification is required.
     * 
     * @return */
    public Set<N> getNodes()
    {
        return nodes;
    }

    /** A simple reconstruct class to rebuild the grid. The set "nodes" is copied due to the fact
     * that this method will allow the modification of nodes while looping. */
    public void reconstruct()
    {
        Iterator<N> it = new HashSet<N>(nodes).iterator();

        while (it.hasNext())
        {
            N node = it.next();

            if (isValidNode(node))
            {
                reconstructNode(node);
            }
            else
            {
                it.remove();
            }
        }
    }

    public boolean isValidNode(Object node)
    {
        return nodeType.isAssignableFrom(node.getClass());
    }

    protected void reconstructNode(N node)
    {

    }

    public void deconstruct()
    {
        synchronized (nodes)
        {
            nodes.clear();
        }
    }

    /** Gets the first node that gets iterated in the node set. Note that the first node may be
     * random as this is a hashset.
     * 
     * @return The first node. */
    public synchronized N getFirstNode()
    {
        synchronized (nodes)
        {
            for (N node : nodes)
            {
                return node;
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + hashCode() + ", Nodes: " + nodes.size() + "]";
    }
}
