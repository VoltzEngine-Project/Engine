package universalelectricity.simulator.grid;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.simulator.parts.NetworkNode;
import universalelectricity.simulator.parts.NetworkPart;
import universalelectricity.simulator.parts.WireJunction;
import universalelectricity.simulator.parts.WirePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple connection path finder that generates network parts while pathing all routes from a single node.
 * Created by Darkguardsman on 8/16/2014.
 */
public class GridPathfinder
{
    /** Network that is being pathed */
    private SimulationGrid network = null;
    /** All parts created by the path finder */
    private List<NetworkPart> parts;
    /** Nodes that have already been pathed */
    private List<NetworkNode> pathed_nodes;

    public GridPathfinder(SimulationGrid network)
    {
        this.network = network;
        pathed_nodes = new ArrayList<NetworkNode>();
        parts = new ArrayList<NetworkPart>();
    }

    /**
     * Starts the path finder to generate network parts from a list of nodes
     * @return list of NetworkParts
     */
    public List<NetworkPart> generateParts()
    {
        NetworkNode firstNode = network.getFirstNode();
        if(firstNode != null)
        {
           path(null, firstNode, null);
        }
        return parts;
    }

    /**
     * Triggers a path finding loop from the node threw all its connections and those node's connections.
     * Does not end until all connections are plotted, and creates new NetworkParts when required
     *
     * @param part - last part created, used to connect new parts to, can be null for first run
     * @param node - current node being pathed, can NOT BE NULL
     * @param side - side we are pathing to from the node, can only be null for first run
     */
    public void path(NetworkPart part, NetworkNode node, ForgeDirection side)
    {
        Map<Object, ForgeDirection> map = node.getConnections();
        NetworkPart nextPart = null;
        pathed_nodes.add(node);
        //Wire is a junction connecting to several paths
        if (map.size() > 2)
        {
            //Create new junction
            nextPart = new WireJunction(network, node);

            //Connection new junction to last part
            if(part instanceof WirePath)
            {
                ((WirePath)part).setConnectionB(nextPart);
            }else if(part instanceof WireJunction)
            {
                ((WireJunction)part).add(nextPart, side);
            }
        }//Wire is a path only connecting in two directions
        else
        {
            //If the last part was a wire add this wire to it
            if(part instanceof WirePath)
            {
                ((WirePath) part).add(node);
                nextPart = part;
            }else
            {
                //Create a new wire and connect it to old part
                nextPart = new WirePath(network, node);
                if(part != null)
                {
                    ((WirePath)nextPart).setConnectionA(part);
                }
                if(part instanceof WireJunction)
                {
                    ((WireJunction)part).add(nextPart, side);
                }
            }
        }

        //Loop threw all connection triggering path() on each instance of NetworkNode
        for (Map.Entry<Object, ForgeDirection> entry : map.entrySet())
        {
            if(entry.getKey() instanceof NetworkNode)
            {
                if(!pathed_nodes.contains(entry.getKey()))
                    path(nextPart, (NetworkNode) entry.getKey(), entry.getValue());
            }
            else if(entry.getKey() instanceof INodeProvider)
            {
                INode providerNode = ((INodeProvider) entry.getKey()).getNode(NetworkNode.class, entry.getValue().getOpposite());
                if(providerNode instanceof NetworkNode)
                {
                    if(!pathed_nodes.contains(entry.getKey()))
                        path(nextPart, (NetworkNode) entry.getKey(), entry.getValue());
                }
            }
            else
            {
                //TODO handle everything else as machines using an input & output logic
            }
        }
    }

    /**
     * Clears out the path finder's results taking it back to a clean state
     */
    public void reset()
    {
        this.parts.clear();
        this.pathed_nodes.clear();
    }
}
