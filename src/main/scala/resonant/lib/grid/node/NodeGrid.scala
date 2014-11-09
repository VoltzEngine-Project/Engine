package resonant.lib.grid.node

import resonant.api.grid.INodeProvider
import resonant.lib.grid.GridNode

/**
 * A node that is part of a larger grid of nodes
 *
 * @tparam A The type this node connects to
 * @author Calclavia
 */
abstract class NodeGrid[A <: NodeGrid[A]](parent: INodeProvider) extends NodeConnector[A](parent) //with INodeGrid
{
  protected var grid: GridNode[A] = null

  override def reconstruct()
  {
    super.reconstruct()

    if (grid == null)
      grid = newGrid

    grid.reconstruct(this.asInstanceOf[A])
  }

  override def deconstruct()
  {
    super.deconstruct()

    if (grid != null)
    {
      grid.remove(this.asInstanceOf[A])
      grid.deconstruct()
    }
  }

  def setGrid(grid: GridNode[_])
  {
    this.grid = grid.asInstanceOf[GridNode[A]]
  }

  def getGrid: GridNode[_] = grid

  def newGrid: GridNode[A]
}
