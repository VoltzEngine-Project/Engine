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

/**
 * A node is an object return by INodeProvider that provides the details of what the specific node does.
 * @param parent - The parent/host node
 * @tparam N - The self node type
 */
abstract class Node[N <: Node[N]](parent: INodeProvider) extends INode
{
  /**
   * All the connections we are connected to. Note that this may not be thread safe for
   * modification unless synchronized.
   *
   * @return Returns all the connections in this node.
   */
  final val connections: Map[N, ForgeDirection] = new util.WeakHashMap()

  protected var grid: Grid[N] = _

  final def getGrid(): Grid[N] =
  {
    if (grid == null)
    {
      grid = newGrid()
      grid.add(this)
    }

    return grid
  }

  protected def newGrid(): Grid[N]

  final def setGrid(grid: Grid[_])
  {
    this.grid = grid.asInstanceOf[Grid[N]]
  }

  /**
   * Called to construct the node. Node must be called in tile validate.
   */
  def reconstruct
  {

    /**
     * TODO: Try inject tile validate and invalidate events so this does not have to be called. Node
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
    connections.keySet().filter(getGrid().isValidNode(_)).foreach(_.connections.remove(this))
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
   * Recache the connections. Node is the default connection implementation for TileEntities.
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

        if (check != null && getClass().isAssignableFrom(check.getClass()) && canConnect(dir, check) && check.canConnect(dir.getOpposite, this))
        {
          connections.put(check.asInstanceOf[N], dir)
        }
      }
    })
  }

  /**
   * Can this node connect with the source?
   *
   * @param from   - Direction coming from.
   * @param source - Object trying to connect with this node. Node should either extend Node or be an object that can interface with the node.
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