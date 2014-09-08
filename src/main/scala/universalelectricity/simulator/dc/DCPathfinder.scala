package universalelectricity.simulator.dc

import java.util
import java.util.{List, Map}

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.{INode, INodeProvider}
import universalelectricity.simulator.grid.component.{WirePath, WireJunction, NetworkPart, NetworkNode}
import universalelectricity.simulator.grid.parts.WirePath

import scala.collection.JavaConversions._

/**
 * A connection path finder that returns all possible paths from point A to itself through a circuit loop.
 * @author Darkguardsman, Calclavia
 */
class DCPathfinder(source: DCNode, grid: DCGrid)
{
  /** All parts created by the path finder */
  private val parts = new util.LinkedList[DCNode]
  /** Nodes that have already been pathed */
  private val pathed_nodes = new util.LinkedList[DCNode]

  /**
   * Starts the path finder to generate network parts from a list of nodes
   * @return list of NetworkParts
   */
  def generateParts: List[NetworkPart] =
  {
    val firstNode: NetworkNode = grid.getFirstNode
    if (firstNode != null)
    {
      find(null, firstNode, null)
    }
    return parts
  }

  /**
   * Triggers recursive pathfinding from the node through all connections.
   * Does not end until all connections are plotted, and creates new NetworkParts when required
   *
   * @param part - last part created, used to connect new parts to, can be null for first run
   * @param node - current node being pathed, can NOT BE NULL
   * @param side - side we are pathing to from the node, can only be null for first run
   */
  def find(part: NetworkPart, node: NetworkNode, side: ForgeDirection)
  {
    val map: Map[AnyRef, ForgeDirection] = node.getConnections
    var nextPart: NetworkPart = null
    pathed_nodes.add(node)
    if (map.size > 2)
    {
      nextPart = new WireJunction(grid, node)
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
        nextPart = new WirePath(grid, node)
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
    for (entry <- map.entrySet)
    {
      if (entry.getKey.isInstanceOf[NetworkNode])
      {
        if (!pathed_nodes.contains(entry.getKey)) find(nextPart, entry.getKey.asInstanceOf[NetworkNode], entry.getValue)
      }
      else if (entry.getKey.isInstanceOf[INodeProvider])
      {
        val providerNode: INode = (entry.getKey.asInstanceOf[INodeProvider]).getNode(classOf[NetworkNode], entry.getValue.getOpposite)
        if (providerNode.isInstanceOf[NetworkNode])
        {
          if (!pathed_nodes.contains(entry.getKey)) find(nextPart, entry.getKey.asInstanceOf[NetworkNode], entry.getValue)
        }
      }
      else
      {
      }
    }
  }
}