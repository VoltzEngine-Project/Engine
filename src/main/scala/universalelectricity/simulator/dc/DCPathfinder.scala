package universalelectricity.simulator.dc

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.INode
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.simulator.grid.SimulationGrid
import universalelectricity.simulator.parts.NetworkNode
import universalelectricity.simulator.parts.NetworkPart
import universalelectricity.simulator.parts.WireJunction
import universalelectricity.simulator.parts.WirePath
import java.util.ArrayList
import java.util.List
import java.util.Map

/**
 * A connection path finder that returns all possible paths from point A to itself through a circuit loop.
 * @author Darkguardsman, Calclavia
 */
class DCPathfinder
{
  /** Network that is being pathed */
  private var network: SimulationGrid = null
  /** All parts created by the path finder */
  private var parts: List[NetworkPart] = null
  /** Nodes that have already been pathed */
  private var pathed_nodes: List[NetworkNode] = null

  def this(network: SimulationGrid)
  {
    this()
    this.network = network
    pathed_nodes = new ArrayList[NetworkNode]
    parts = new ArrayList[NetworkPart]
  }

  /**
   * Starts the path finder to generate network parts from a list of nodes
   * @return list of NetworkParts
   */
  def generateParts: List[NetworkPart] =
  {
    val firstNode: NetworkNode = network.getFirstNode
    if (firstNode != null)
    {
      path(null, firstNode, null)
    }
    return parts
  }

  /**
   * Triggers a path finding loop from the node threw all its connections and those node's connections.
   * Does not end until all connections are plotted, and creates new NetworkParts when required
   *
   * @param part - last part created, used to connect new parts to, can be null for first run
   * @param node - current node being pathed, can NOT BE NULL
   * @param side - side we are pathing to from the node, can only be null for first run
   */
  def path(part: NetworkPart, node: NetworkNode, side: ForgeDirection)
  {
    val map: Map[AnyRef, ForgeDirection] = node.getConnections
    var nextPart: NetworkPart = null
    pathed_nodes.add(node)
    if (map.size > 2)
    {
      nextPart = new WireJunction(network, node)
      if (part.isInstanceOf[WirePath])
      {
        (part.asInstanceOf[WirePath]).setConnectionB(nextPart)
      }
      else if (part.isInstanceOf[WireJunction])
      {
        (part.asInstanceOf[WireJunction]).add(nextPart, side)
      }
    }
    else
    {
      if (part.isInstanceOf[WirePath])
      {
        (part.asInstanceOf[WirePath]).add(node)
        nextPart = part
      }
      else
      {
        nextPart = new WirePath(network, node)
        if (part != null)
        {
          (nextPart.asInstanceOf[WirePath]).setConnectionA(part)
        }
        if (part.isInstanceOf[WireJunction])
        {
          (part.asInstanceOf[WireJunction]).add(nextPart, side)
        }
      }
    }
    import scala.collection.JavaConversions._
    for (entry <- map.entrySet)
    {
      if (entry.getKey.isInstanceOf[NetworkNode])
      {
        if (!pathed_nodes.contains(entry.getKey)) path(nextPart, entry.getKey.asInstanceOf[NetworkNode], entry.getValue)
      }
      else if (entry.getKey.isInstanceOf[INodeProvider])
      {
        val providerNode: INode = (entry.getKey.asInstanceOf[INodeProvider]).getNode(classOf[NetworkNode], entry.getValue.getOpposite)
        if (providerNode.isInstanceOf[NetworkNode])
        {
          if (!pathed_nodes.contains(entry.getKey)) path(nextPart, entry.getKey.asInstanceOf[NetworkNode], entry.getValue)
        }
      }
      else
      {
      }
    }
  }
}