package resonant.lib.grid

import resonant.lib.grid.node.NodeGrid

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
   * Rebuild the node list starting from the first node and recursively iterating through its connections.
   */
  def reconstruct(first: N)
  {
    if (!getNodes.contains(first))
    {
      //TODO: Reconstruct may be called MANY times unnecessarily multiple times. Add check to prevent extra calls
      getNodes.clear()
      populate(first)
    }
  }

  /**
   * Populates the node list
   */
  protected def populate(node: N, prev: N = null.asInstanceOf[N])
  {
    if (!getNodes.contains(node) && isValidNode(node))
    {
      add(node)
      reconstructNode(node)
      node.connections.foreach(n => populate(n, node))
    }
  }

  protected def populateNode(node: N)
  {
    node.setGrid(this)
  }

  override def deconstruct()
  {
    for (node <- getNodes)
    {
      node.setGrid(null)
    }

    super.deconstruct()
  }
}