package universalelectricity.simulator.grid;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.simulator.grid.component.NetworkNode;
import universalelectricity.simulator.grid.component.NetworkPart;
import universalelectricity.simulator.grid.component.WireJunction;
import universalelectricity.simulator.grid.component.WirePath;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Simple connection path finder that generates grid parts while pathing all routes from a single node.
 * @author Darkguardsman, Calclavia
 */
public class GridPathfinder
{
	/**
	 * Network that is being pathed
	 */
	private SimulatedGrid grid;
	/**
	 * All parts created by the path finder
	 */
	private List<NetworkPart> parts = new LinkedList<NetworkPart>();
	/**
	 * Nodes that have already been pathed
	 */
	private List<NetworkNode> pathNodes = new LinkedList<NetworkNode>();

	public GridPathfinder(SimulatedGrid grid)
	{
		this.grid = grid;
	}

	/**
	 * Starts the path finder to generate grid parts from a list of nodes
	 *
	 * @return list of NetworkParts
	 */
	public List<NetworkPart> generateParts()
	{
		NetworkNode firstNode = grid.getFirstNode();
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
	 * @param part - last part created, used to connect new parts to, can be null for first run
	 * @param currentNode - current node being pathed
	 * @param side - side we are pathing to from the node, can only be null for first run
	 */
	public void path(NetworkPart part, NetworkNode currentNode, ForgeDirection side)
	{
		Map<Object, ForgeDirection> connections = currentNode.getConnections();
		NetworkPart nextPart = null;
		pathNodes.add(currentNode);

		//More than two connections, wire is a junction connecting to several paths
		if (connections.size() > 2)
		{
			//Create new junction
			nextPart = new WireJunction(grid, currentNode);

			//Connection new junction to last part
			if (part instanceof WirePath)
			{
				((WirePath) part).setConnectionB(nextPart);
			}
			else if (part instanceof WireJunction)
			{
				((WireJunction) part).add(nextPart, side);
			}
		}//Wire is a path only connecting in two directions
		else
		{
			//If the last part was a wire add this wire to it
			if (part instanceof WirePath)
			{
				((WirePath) part).add(currentNode);
				nextPart = part;
			}
			else
			{
				//Create a new wire and connect it to old part
				nextPart = new WirePath(grid, currentNode);
				if (part != null)
				{
					((WirePath) nextPart).setConnectionA(part);
				}
				if (part instanceof WireJunction)
				{
					((WireJunction) part).add(nextPart, side);
				}
			}
		}

		//Loop threw all connection triggering path() on each instance of NetworkNode
		for (Map.Entry<Object, ForgeDirection> entry : connections.entrySet())
		{
			if (entry.getKey() instanceof NetworkNode)
			{
				if (!pathNodes.contains(entry.getKey()))
				{
					path(nextPart, (NetworkNode) entry.getKey(), entry.getValue());
				}
			}
			else if (entry.getKey() instanceof INodeProvider)
			{
				INode providerNode = ((INodeProvider) entry.getKey()).getNode(NetworkNode.class, entry.getValue().getOpposite());
				if (providerNode instanceof NetworkNode)
				{
					if (!pathNodes.contains(entry.getKey()))
					{
						path(nextPart, (NetworkNode) entry.getKey(), entry.getValue());
					}
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
		this.pathNodes.clear();
	}
}
