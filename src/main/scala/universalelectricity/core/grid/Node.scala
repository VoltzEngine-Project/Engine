package universalelectricity.core.grid

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import universalelectricity.api.core.grid.{INodeProvider, INode}
import universalelectricity.core.transform.vector.Vector3
import java.util.Map
import scala.collection.convert.wrapAll._
import java.util
import net.minecraftforge.common.util.ForgeDirection

abstract class Node(parent: INodeProvider) extends INode
{
  protected final val connections: Map[this.type, ForgeDirection] = new util.WeakHashMap[this.type, ForgeDirection]

  protected var grid: Grid[this.type] = _

  final def getGrid(): Grid[this.type] =
  {
    if (grid == null)
    {
      grid = newGrid()
      grid.add(this)
    }

    return grid
  }

  protected def newGrid(): Grid[this.type]

  final def setGrid(grid: Grid[_])
  {
    this.grid = grid.asInstanceOf[Grid[this.type]]
  }

  /**
   * Called to construct the node. This must be called in tile validate.
   */
  def reconstruct
  {

    /**
     * TODO: Try inject tile validate and invalidate events so this does not have to be called. This
     * constructs the node. It should be called whenever the connections of the node are updated OR
     * when the node is first initiated and can access its connections.
     */
    recache()
    getGrid.add(this)
    getGrid.reconstruct()
  }

  /**
   * Called to deconstruct the node. THis must be called in tile invalidate.
   */
  def deconstruct()
  {
    //Remove all connection references to the current node.
    connections.keySet().filter(getGrid().isValidNode(_)).foreach(_.getConnections().remove(this))
    getGrid.remove(this)
    getGrid.deconstruct()
  }

  /**
   * Called for a node to recache all its connections.
   */
  final def recache()
  {
    doRecache()
  }

  /**
   * Recache the connections. This is the default connection implementation for TileEntities.
   */
  def doRecache()
  {
    connections.clear()

    ForgeDirection.VALID_DIRECTIONS.foreach(dir =>
    {
      val tile = (position + dir).getTileEntity(world)

      if (tile.isInstanceOf[INodeProvider])
      {
        val check = tile.asInstanceOf[INodeProvider].getNode(getClass(), dir.getOpposite)

        if (check.isInstanceOf[this.type] && canConnect(dir, check) && check.canConnect(dir.getOpposite, this))
        {
          connections.put(check.asInstanceOf[this.type], dir)
        }
      }
    })
  }

  /**
   * All the connections we are connected to. Note that this may not be thread safe for
   * modification unless synchronized.
   *
   * @return Returns all the connections in this node.
   */
  def getConnections(): Map[this.type, ForgeDirection] = connections

  /**
   * Can this node connect with the source?
   *
   * @param from   - Direction coming from.
   * @param source - Object trying to connect with this node. This should either extend Node or be an object that can interface with the node.
   */
  def canConnect(from: ForgeDirection, source: AnyRef): Boolean =
  {
    return false
  }

  /**
   * Must be called to load the node's data.
   */
  def load(nbt: NBTTagCompound)
  {
  }

  /**
   * Must be called to save the node's data.
   */
  def save(nbt: NBTTagCompound)
  {
  }

  def world: World =
  {
    //TODO: Forge Mulitpart
    return if ((parent.isInstanceOf[TileEntity])) (parent.asInstanceOf[TileEntity]).getWorldObj else null
  }

  def position: Vector3 =
  {
    return if ((parent.isInstanceOf[TileEntity])) new Vector3(parent.asInstanceOf[TileEntity]) else null
  }

  override def toString: String =
  {
    return getClass.getSimpleName + "[" + hashCode + ", Connections: " + connections.size + ", Grid:" + getGrid + "]"
  }

}