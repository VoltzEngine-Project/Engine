package resonant.lib.grid.branch

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{IGrid, INodeGrid, INodeProvider}
import resonant.engine.References
import resonant.lib.grid.branch.part.Branch
import resonant.lib.grid.node.Node
import resonant.lib.prefab.TConnector

import scala.beans.BeanProperty
;

/**
 * A node that is part of a branch
 * @author DarkCow
 */
class NodeBranchPart(parent: INodeProvider) extends Node(parent) with TConnector[NodeBranchPart] with INodeGrid
{
  @BeanProperty
  var branch : Branch = null

  var grid : BranchedGrid = null

  /** Gets the branch that contains this node
  def getBranch : Branch = branch

   Sets the branch that will contain this node
  def setBranch(b : Branch)
  {
    branch = b
  }*/

  /** Sets the grid reference */
  override def setGrid(grid: IGrid[_]):Unit =
  {
    if(grid.isInstanceOf[BranchedGrid])
      this.grid = grid.asInstanceOf[BranchedGrid]
  }

  /** Gets the grid reference */
  override def getGrid: BranchedGrid =
  {
    if(grid == null)
    {
      grid = new BranchedGrid()
      grid.add(this)
    }
    return grid
  }

  /** Is this connector allowed to connect to any side
    * @param connector - any connecting object, Most likely TileEntity, Node, INodeProvider */
  override protected def isValidConnector(connector: NodeBranchPart): Boolean =
  {
    return connector != null;
  }

  override def reconstruct()
  {
    References.LOGGER.info("NodeBranchPart: Reconstruct")
    super.reconstruct()
    buildConnections()
  }

  override def connect(obj: NodeBranchPart, dir: ForgeDirection)
  {
    References.LOGGER.info("NodeBranchPart: connect(" + obj + ", " + dir +")")
    super.connect(obj, dir)
    if(obj.getGrid != this.getGrid)
      obj.getGrid.merge(getGrid)
  }

  override def getNodeFromConnection(provider: INodeProvider, dir: ForgeDirection) : NodeBranchPart =
  {
    return provider.getNode(classOf[NodeBranchPart], dir)
  }
}