package resonant.lib.prefab

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.IConnector
import resonant.lib.transform.vector.TVectorWorld
import resonant.lib.wrapper.BitmaskWrapper._

/**
 * Created by robert on 11/12/2014.
 */
trait TConnector extends IConnector with TVectorWorld
{
  /** The bitmask containing sides that this node may connect to */
  var allowedConnections = 0x3F

  protected var _connectedMask = 0x00

  /** Map of connections */
  private val connectionMap : java.util.Map[AnyRef, ForgeDirection] = new java.util.HashMap[AnyRef, ForgeDirection]


  override def getConnections : java.util.Map[AnyRef, ForgeDirection] = connectionMap

  //////////////////////////////
  ///   Connection Rules
  //////////////////////////////

  override def canConnect(connection: AnyRef, from: ForgeDirection): Boolean = isValidConnector(connection) && canConnect(from)

  /** Is this connector allowed to connect to any side
   * @param connector - any connecting object, Most likely TileEntity, Node, INodeProvider */
  protected def isValidConnector(connector : Object) : Boolean

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
  def connect(obj: Object, dir: ForgeDirection)
  {
    connectionMap.put(obj, dir)
    _connectedMask = _connectedMask.openMask(dir)
  }

  /** Removes the connection */
  def disconnect(obj: Object)
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
      val tile = loc.getTileEntity
      if(canConnect(tile, dir.getOpposite))
      {
        connect(loc, dir)
      }
    }
  }
}
