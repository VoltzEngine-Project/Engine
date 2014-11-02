package resonant.lib.grid.node

import java.lang.{Iterable => JIterable}
import java.util.{Map => JMap, Set => JSet}

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.INodeProvider

import scala.collection.convert.wrapAll._
import scala.collection.mutable

/**
 * A node that can connect to other nodes.
 *
 * @param parent - The INodeProvider that contains this node.
 * @tparam A - The type of objects this node can connect to.
 * @author Darkguardsman, Calclavia
 */
abstract class NodeConnector[A](parent: INodeProvider) extends Node(parent) // with IConnector
{
  protected var connectionMask = 0x3F

  /**
   * Connections to other nodes specifically.
   */
  private val connectionMap = mutable.WeakHashMap.empty[A, ForgeDirection]

  def canConnect(obj: AnyRef, from: ForgeDirection): Boolean = obj != null && isValidConnection(obj) && canConnect(from)

  def canConnect(from: ForgeDirection): Boolean = ((connectionMask & (1 << from.ordinal)) != 0) || from == ForgeDirection.UNKNOWN

  def isValidConnection(obj: AnyRef): Boolean = obj != null && obj.getClass.isAssignableFrom(getClass)

  def connect[B <: A](obj: A, dir: ForgeDirection) = connectionMap.put(obj, dir)

  def disconnect[B <: A](obj: B) = connectionMap.remove(obj)

  def connections: JSet[A] = connectionMap.keys.toSet[A]

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