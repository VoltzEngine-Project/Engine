package resonant.lib.grid.node

import java.util.WeakHashMap

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INodeProvider, INode, IConnector}

/**
 * A node that can connect to other nodes.
 *
 * TODO: Consider adding generics to restrict the node type
 *
 * @author Darkguardsman, Calclavia
 */
abstract class NodeConnector(parent: INodeProvider) extends Node(parent) with IConnector
{
  protected var connectionMap: Byte = 0x3F

  /**
   * Connections to other nodes specifically.
   */
  protected val connections = new WeakHashMap[AnyRef, ForgeDirection]

  def getConnections = connections

  def canConnect(obj: AnyRef, from: ForgeDirection) = obj != null && isValidConnection(obj) && canConnect(from)

  def canConnect(from: ForgeDirection) = ((connectionMap & (1 << from.ordinal)) != 0) || from == ForgeDirection.UNKNOWN

  def isValidConnection(obj: AnyRef): Boolean = obj != null && obj.getClass.isAssignableFrom(getClass)

  /**
   * Called during reconstruct to build the connection map. This is a general way used to search all adjacent TileEntity to see and try to connect to it.
   */
  override def reconstruct()
  {
    super.reconstruct()

    connections.clear()

    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (canConnect(direction))
      {
        val tile: TileEntity = position.add(direction).getTileEntity
        val node: INode = getNodeFrom(tile, direction.getOpposite)
        if (node != null)
        {
          addConnection(node, direction)
        }
      }
    }
  }

  override def deconstruct()
  {
    super.deconstruct()

    if (connections != null)
    {
      connections.clear
    }
  }

  protected def getNodeFrom(tile: TileEntity, from: ForgeDirection): INode =
  {
    if (tile.isInstanceOf[INodeProvider])
    {
      val node: INode = (tile.asInstanceOf[INodeProvider]).getNode(getRelativeClass, from)
      if (node != null)
      {
        return node
      }
    }
    return null
  }

  /**
   * Called to add an object to the connection map. Override this to update connection masks for client packets if needed.
   */
  protected def addConnection(obj: AnyRef, dir: ForgeDirection)
  {
    connections.put(obj, dir)
  }

  /**
   * The class used to compare when making connections
   */
  protected def getRelativeClass: Class[_ <: INode] = getClass
}