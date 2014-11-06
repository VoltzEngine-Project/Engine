package resonant.lib.grid.electric.macroscopic.part;

import java.util.HashSet;
import java.util.Set;

/** Connection point of several connections
 * is not limited to 6 sides of a block
 * @author DarkCow
 */
public class Junction extends Part
{
    private Set<Part> connections;

    public Junction()
    {
        connections = new HashSet<Part>();
    }

    /** Set of connections, not limited to 6 */
    public Set<Part> getConnections()
    {
        return connections;
    }

    /** Adds a connection to the junction */
    public void addConnection(Part part)
    {
        if(!connections.contains(part))
            connections.add(part);
    }

    /** Removes a connection from the junction */
    public void removeConnection(Part part)
    {
        connections.remove(part);
    }

    @Override
    public boolean hasMinimalConnections()
    {
        return getConnections().size() > 2;
    }
}
