package resonant.lib.grid.node

import resonant.api.grid.{IGrid, INodeGrid, INodeProvider}

/**
 * A node that implements a grid
 * @tparam A The type this node connects to
 * @author Calclavia
 */
abstract class NodeGrid[A <: AnyRef](parent: INodeProvider) extends NodeConnector[A](parent) with INodeGrid
{
  protected var grid: IGrid[A] = null

  override def deconstruct()
  {
    super.deconstruct()

    if (grid != null)
      grid.remove(this.asInstanceOf[A])
  }

  override def reconstruct()
  {
    super.reconstruct()

    if (grid != null)
    {
      grid.add(this.asInstanceOf[A])
    }
  }

  override def setGrid(grid: IGrid[_])
  {
    this.grid = grid.asInstanceOf[IGrid[A]]
  }

  override def getGrid: IGrid[_] = grid
}
