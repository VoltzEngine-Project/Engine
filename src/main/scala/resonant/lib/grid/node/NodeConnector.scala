package resonant.lib.grid.node

import java.util.{Map => JMap, Set => JSet}

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INodeConnector, INodeProvider}
import resonant.lib.wrapper.BitmaskWrapper._

import scala.collection.convert.wrapAll._
import scala.collection.mutable

/**
 * A node that can connect to other nodes.
 *
 * @param parent - The INodeProvider that contains this node.
 * @tparam A - The type of objects this node can connect to.
 * @author Darkguardsman, Calclavia
 */
abstract class NodeConnector[A <: AnyRef](parent: INodeProvider) extends Node(parent) with INodeConnector[A]
{
  /** The bitmask containing sides that this node may connect to */
  var connectionMask = 0x3F

  /** The bitmask containing the connected sides */
  private var _connectedMask = 0x00

  def connectedMask = _connectedMask

  /** Functional event handler when the connection changes */
  var onConnectionChanged: () => Unit = () => ()

  var isInvalid = false

  /**
   * Connections to other nodes specifically.
   */
  private val connectionMap = mutable.WeakHashMap.empty[A, ForgeDirection]

  /**
   * Can this node connect with another node?
   * @param other - Most likely a node, but it can also be another object
   * @param from - Direction of connection
   * @return True connection is allowed
   */
  override def canConnect[B <: A](other: B, from: ForgeDirection): Boolean = isValidConnection(other) && canConnect(from)

  def canConnect(from: ForgeDirection): Boolean = connectionMask.mask(from) || from == ForgeDirection.UNKNOWN

  //TODO: This getClass.isAssignableFrom has issues.
  def isValidConnection(other: AnyRef): Boolean = other != null //&& other.getClass.isAssignableFrom(getClass)

  def connect[B <: A](obj: B, dir: ForgeDirection)
  {
    connectionMap.put(obj, dir)
    _connectedMask = _connectedMask.openMask(dir)
    onConnectionChanged()
  }

  def disconnect[B <: A](obj: B)
  {
    _connectedMask = _connectedMask.closeMask(connectionMap(obj))
    connectionMap.remove(obj)
    onConnectionChanged()
  }

  def disconnect(dir: ForgeDirection)
  {
    _connectedMask = _connectedMask.closeMask(dir)
    connectionMap.removeAll(connectionMap.filter(_._2 == dir))
    onConnectionChanged()
  }

  def clearConnections()
  {
    connectionMap.clear()
    _connectedMask = 0x00
  }

  override def connections: JSet[A] = connectionMap.keys.toSet[A]

  def directionMap: JMap[A, ForgeDirection] = connectionMap

  /**
   * Called to rebuild the connection map.
   * This is a general way used to search all adjacent TileEntity and attempt a connection
   */
  override def reconstruct()
  {
    super.reconstruct()
    clearConnections()
    rebuild()
    onConnectionChanged()
    isInvalid = false
  }

  def rebuild()
  {

  }

  override def deconstruct()
  {
    super.deconstruct()
    clearConnections()
    isInvalid = true
  }

  override def toString: String = getClass.getSimpleName + "[Connections: " + connections.size + "]"
}