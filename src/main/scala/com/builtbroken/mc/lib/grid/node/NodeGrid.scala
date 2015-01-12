package com.builtbroken.mc.lib.grid.node

import com.builtbroken.mc.api.tile.ITileNodeProvider
import com.builtbroken.mc.lib.grid.GridNode
import scala.collection.convert.wrapAll._
/**
 * A node that is part of a larger grid of nodes
 *
 * @tparam A The type this node connects to
 * @author Calclavia
 */
abstract class NodeGrid[A <: NodeGrid[A]](parent: ITileNodeProvider) extends NodeConnector[A](parent) //with INodeGrid
{
  protected var _grid: GridNode[A] = null

  /** Event called after a grid reconstructs itself */
  var onGridReconstruct: () => Unit = () => ()

  override def reconstruct()
  {
    super.onJoinWorld()
    grid.reconstruct(this.asInstanceOf[A])
  }

  override def deconstruct()
  {
    /**
     * Attempt to remove connections to this node in other nodes
     */
    connections.foreach(_.disconnect(this.asInstanceOf[A]))
    super.onLeaveWorld()
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
