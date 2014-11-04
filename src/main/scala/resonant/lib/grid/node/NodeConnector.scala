package resonant.lib.grid.node

import java.lang.{Iterable => JIterable}
import java.util.{Map => JMap, Set => JSet}

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INodeConnector, INodeProvider}

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
  var connectionMask = 0x3F

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

  def canConnect(from: ForgeDirection): Boolean = ((connectionMask & (1 << from.ordinal)) != 0) || from == ForgeDirection.UNKNOWN

  def isValidConnection(obj: AnyRef): Boolean = obj != null && obj.getClass.isAssignableFrom(getClass)

  def connect[B <: A](obj: B, dir: ForgeDirection): Unit = connectionMap.put(obj, dir)

  def disconnect[B <: A](obj: B): Unit = connectionMap.remove(obj)

  override def connections: JSet[A] = connectionMap.keys.toSet[A]

  def directionMap: JMap[A, ForgeDirection] = connectionMap

  /**
   * Called to rebuild the connection map.
   * This is a general way used to search all adjacent TileEntity and attempt a connection
   */
  override def reconstruct()
  {
    super.reconstruct()
    connectionMap.clear()
  }

  override def deconstruct()
  {
    super.deconstruct()
    connectionMap.clear()
  }
}