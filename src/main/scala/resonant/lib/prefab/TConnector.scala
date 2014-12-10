package resonant.lib.prefab

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.tile.{IConnector, INodeProvider}
import resonant.api.tile.node.INode
import resonant.lib.transform.vector.{TVectorWorld, VectorWorld}
import resonant.lib.wrapper.BitmaskWrapper._

/**
 * Created by robert on 11/12/2014.
 */
trait TConnector[N] extends IConnector[N] with TVectorWorld
{
  /** The bitmask containing sides that this node may connect to */
  var allowedConnections = 0x3F

  protected var _connectedMask = 0x00

  /** Map of connections */
  private val connectionMap : java.util.Map[N, ForgeDirection] = new java.util.HashMap[N, ForgeDirection]


  override def getConnections : java.util.Map[N, ForgeDirection] = connectionMap

  //////////////////////////////
  ///   Connection Rules
  //////////////////////////////

  override def canConnect(connection: N, from: ForgeDirection): Boolean = isValidConnector(connection) && canConnect(from)

  /** Is this connector allowed to connect to any side
   * @param connector - any connecting object, Most likely TileEntity, Node, INodeProvider */
  protected def isValidConnector(connector : N) : Boolean

  /** Can any connector connect to this side
   * @param from - side connecting from
   * @return true if connection is allowed */
  def canConnect(from: ForgeDirection): Boolean = allowedConnections.mask(from) || from == ForgeDirection.UNKNOWN


  ////////////////////////////
  /// Connection Handlers
  ////////////////////////////

  /**
   * Connectes the obj to the side
   * @param obj - connection
   * @param dir - from
   */
  def connect(obj: N, dir: ForgeDirection)
  {
    connectionMap.put(obj, dir)
    _connectedMask = _connectedMask.openMask(dir)
  }

  /** Removes the connection */
  def disconnect(obj: N)
  {
    _connectedMask = _connectedMask.closeMask(connectionMap.get(obj))
    connectionMap.remove(obj)
  }

  /** Removes the connection */
  def disconnect(dir: ForgeDirection)
  {
    _connectedMask = _connectedMask.closeMask(dir)
    connectionMap.clear()
  }

  /** Removes all connections */
  def clearConnections()
  {
    connectionMap.clear()
    _connectedMask = 0x00
  }

  /** Call to update connections, should be call on world join*/
  def buildConnections()
  {
    for(dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      val loc = toVectorWorld + dir
      updateConnection(dir, loc)
    }
  }

  def updateConnection(dir: ForgeDirection, loc: VectorWorld)
  {
    val tile = loc.getTileEntity
    if(tile != null)
    {
      if(tile.isInstanceOf[INodeProvider])
      {
        val node = getNodeFromConnection(tile.asInstanceOf[INodeProvider], dir)
        if(node.isInstanceOf[N])
        {
          if (canConnect(node.asInstanceOf[N], dir.getOpposite))
          {
            connect(node.asInstanceOf[N], dir)
            return
          }
        }
      }
    }
  }

  def getNodeFromConnection(provider: INodeProvider, dir: ForgeDirection) : INode = null
}
