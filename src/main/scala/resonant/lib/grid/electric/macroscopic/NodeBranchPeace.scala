package resonant.lib.grid.electric.macroscopic

import resonant.api.grid.{IGrid, INodeGrid, INodeProvider}
import resonant.lib.grid.electric.macroscopic.part.Branch
import resonant.lib.prefab.TConnector
import resonant.lib.grid.node.Node;

/**
 * Created by robert on 11/12/2014.
 */
class NodeBranchPeace(parent: INodeProvider) extends Node(parent) with TConnector with INodeGrid
{
  var branch : Branch
  var grid : BranchedGrid

  /** Gets the branch that contains this node */
  def getBranch : Branch = branch

  /** Sets the branch that will contain this node */
  def setBranch(b : Branch)
  {
    branch = b
  }

  /** Sets the grid reference */
  override def setGrid(grid: BranchedGrid)
  {
    this.grid = grid
  }

  /** Gets the grid reference */
  override def getGrid: IGrid[BranchedGrid] =
  {
    if(grid == null)
    {
      grid = new BranchedGrid[NodeBranchPeace]()
      grid.add(this)
    }
    return grid
  }
}