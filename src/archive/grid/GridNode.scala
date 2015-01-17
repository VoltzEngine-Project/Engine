package com.builtbroken.mc.lib.grid

import com.builtbroken.mc.lib.grid.node.NodeGrid

import scala.collection.convert.wrapAll._

/**
 * A grid that contains nodes where all nodes are interconnected
 *
 * @param N The type of node we can connect
 * @author Calclavia
 */
class GridNode[N <: NodeGrid[N]](node: Class[N]) extends Grid[N](node)
{
  /**
   * Is the grid dead?
   */
  var dead = false

  /**
   * Rebuild the node list starting from the first node and recursively iterating through its connections.
   */
  def reconstruct(first: N)
  {
    //TODO: Reconstruct may be called MANY times unnecessarily multiple times. Add check to prevent extra calls
    getNodes.clear()
    populate(first)
    getNodes.foreach(_.onGridReconstruct())
  }

  /**
   * Populates the node list
   */
  protected def populate(node: N, prev: N = null.asInstanceOf[N])
  {
    if (!getNodes.contains(node) && isValidNode(node))
    {
      add(node)
      populateNode(node, prev)
      node.connections.foreach(n => populate(n, node))
    }
  }

  protected def populateNode(node: N, prev: N = null.asInstanceOf[N])
  {
    if (node.grid != this)
    {
      node.grid.remove(node)
      node.setGrid(this)
    }
  }

  def deconstruct(first: N)
  {
    remove(first)
    first.setGrid(null)

    getNodes.toList.foreach(n =>
    {
      if (n.grid == this)
      {
        n.setGrid(null)
        n.onJoinWorld()
      }
    })

    //This grid is now dead
    dead = true
  }
}