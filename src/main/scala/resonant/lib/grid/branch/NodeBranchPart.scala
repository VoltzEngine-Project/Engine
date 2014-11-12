package resonant.lib.grid.branch

import resonant.api.grid.{IGrid, INodeGrid, INodeProvider}
import resonant.lib.grid.node.Node
import resonant.lib.prefab.TConnector;

/**
 * Created by robert on 11/12/2014.
 */
class NodeBranchPart(parent: INodeProvider) extends Node(parent) with TConnector with INodeGrid
{
  var branch : Branch = null
  var grid : BranchedGrid = null

  /** Gets the branch that contains this node */
  def getBranch : Branch = branch

  /** Sets the branch that will contain this node */
  def setBranch(b : Branch)
  {
    branch = b
  }

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
  override protected def isValidConnector(connector: Object): Boolean =
  {
    return connector != null && connector.isInstanceOf[NodeBranchPart];
  }
}