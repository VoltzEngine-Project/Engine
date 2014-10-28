package resonant.lib.grid.node

import resonant.api.grid.{IGridNode, IGrid, INodeProvider}

/**
 * A node that implements a grid
 * @author Calclavia
 */
abstract class NodeGrid(parent: INodeProvider) extends NodeConnector(parent) with IGridNode
{
  protected var grid: IGrid[_] = null

  def this(parent: INodeProvider)
  {
    this()
    `super`(parent)
  }

  override def deconstruct()
  {
    super.deconstruct()

    if (grid != null)
    {
      grid.remove(this)
    }
  }

  override def reconstruct()
  {
    super.reconstruct()

    if (grid != null)
    {
      grid.add(this)
    }
  }

  def setGrid(grid: IGrid[_])
  {
    this.grid = grid
  }

  def getGrid: IGrid[_] =
  {
    return grid
  }
}
