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
  protected var _grid: GridNode[A] = null

  /** Event called after a grid reconstructs itself */
  var onGridReconstruct: () => Unit = () => ()

  override def reconstruct()
  {
    super.reconstruct()
    grid.reconstruct(this.asInstanceOf[A])
  }

  override def deconstruct()
  {
    super.deconstruct()
    grid.deconstruct(this.asInstanceOf[A])
  }

  def setGrid(grid: GridNode[_])
  {
    this._grid = grid.asInstanceOf[GridNode[A]]
  }

  def grid: GridNode[A] =
  {
    if (_grid == null)
      _grid = newGrid

    _grid
  }

  protected def newGrid: GridNode[A]
}
