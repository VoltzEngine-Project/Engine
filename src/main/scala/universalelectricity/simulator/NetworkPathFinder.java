package universalelectricity.simulator;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.simulator.peaces.NetworkNode;
import universalelectricity.simulator.peaces.NetworkPart;
import universalelectricity.simulator.peaces.WireJunction;
import universalelectricity.simulator.peaces.WirePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkPathFinder
{
    Network network = null;
    List<NetworkPart> parts = new ArrayList<NetworkPart>();

    public NetworkPathFinder(Network network)
    {
        this.network = network;
    }

    public List<NetworkPart> generateParts()
    {
        NetworkNode firstNode = network.getFirstNode();
        if(firstNode != null)
        {
           path(null, firstNode, null);
        }
        return parts;
    }

    public void path(NetworkPart part, NetworkNode node, ForgeDirection side)
    {
        Map<Object, ForgeDirection> map = node.getConnections();
        NetworkPart nextPart = null;

        //Wire is a junction connecting to several paths
        if (map.size() > 2)
        {
            nextPart = new WireJunction(network.simulator, node);
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
            if(part instanceof WirePath)
            {
                ((WirePath) part).add(node);
                nextPart = part;
            }else
            {
                nextPart = new WirePath(network.simulator, node);
                if(part instanceof WireJunction)
                {
                    ((WireJunction)part).add(nextPart, side);
                }
            }
        }
        for (Map.Entry<Object, ForgeDirection> entry : map.entrySet())
        {
            if(entry.getKey() instanceof NetworkNode)
            {
                path(nextPart, (NetworkNode) entry.getKey(), entry.getValue());
            }
            else if(entry.getKey() instanceof INodeProvider)
            {
                INode providerNode = ((INodeProvider) entry.getKey()).getNode(NetworkNode.class, entry.getValue().getOpposite());
                if(providerNode instanceof NetworkNode)
                {
                    path(nextPart, (NetworkNode) entry.getKey(), entry.getValue());
                }
            }
            else
            {
                //TODO handle everything else as machines using an input & output logic
            }
        }
    }
}
