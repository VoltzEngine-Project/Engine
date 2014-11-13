package resonant.lib.grid.branch;

import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.grid.INode;
import resonant.api.grid.INodeProvider;
import resonant.lib.grid.branch.part.Branch;
import resonant.lib.grid.branch.part.Junction;
import resonant.lib.grid.branch.part.Part;

import java.util.*;

/**
 * Simple connection path finder that generates grid parts while pathing all routes from a single node.
 *
 * @author Darkguardsman, Calclavia
 */
public class GridPathfinder
{
    /**
     * Network that is being pathed
     */
    private BranchedGrid grid;

    /**
     * All parts created by the path finder
     */
    private Set<Part> parts = new HashSet<Part>();

    /**
     * Nodes that have already been pathed
     */
    private List<NodeBranchPart> pathNodes = new LinkedList<NodeBranchPart>();

    public GridPathfinder(BranchedGrid grid)
    {
        this.grid = grid;
    }

    /**
     * Starts the path finder to generate grid parts from a list of nodes
     *
     * @return list of NetworkParts
     */
    public Set<Part> generateParts()
    {
        NodeBranchPart firstNode = grid.getFirstNode();
        if (firstNode != null)
        {
            path(null, firstNode, null);
        }
        return parts;
    }

    /**
     * Triggers a pathfinding loop from the node through all its connections and those node's connections.
     * Does not end until all connections are plotted, and creates new NetworkParts when required
     *
     * @param part        - last part created, used to connect new parts to, can be null for first run
     * @param currentNode - current node being pathed
     * @param side        - side we are pathing to from the node, can only be null for first run
     */
    public void path(Part part, NodeBranchPart currentNode, ForgeDirection side)
    {
        Map<NodeBranchPart, ForgeDirection> connections = currentNode.getConnections();
        Part nextPart = null;
        pathNodes.add(currentNode);

        //More than two connections, wire is a junction connecting to several paths
        if (connections.size() > 2)
        {
            //Connection new junction to last part
            if (part instanceof Branch)
            {
                ((Branch) part).setConnectionB(nextPart);

                //Create new junction
                nextPart = new Junction();
                nextPart.add(currentNode);
                ((Junction) nextPart).addConnection(part);
            }//If we have another junction point merge it into a single junction
            else if (part instanceof Junction)
            {
                ((Junction) part).add(currentNode);
                nextPart = part;
            }
        }//Wire is a path only connecting in two directions
        else
        {
            //If the last part was a wire add this wire to it
            if (part instanceof Branch)
            {
                ((Branch) part).add(currentNode);
                nextPart = part;
            } else
            {
                //Create a new wire and connect it to old part
                nextPart = new Branch();
                ((Branch) nextPart).add(currentNode);
                if (part != null)
                {
                    ((Branch) nextPart).setConnectionA(part);
                }
                if (part instanceof Junction)
                {
                    ((Junction) part).addConnection(nextPart);
                }
            }
        }

        //Loop threw all connection triggering path() on each instance of NetworkNode
        for (Map.Entry<NodeBranchPart, ForgeDirection> entry : connections.entrySet())
        {
            if (entry.getKey() != null)
            {
                if (!pathNodes.contains(entry.getKey()))
                {
                    path(nextPart, (NodeBranchPart) entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * Clears out the path finder's results taking it back to a clean state
     */
    public void reset()
    {
        this.parts.clear();
        this.pathNodes.clear();
    }
}
