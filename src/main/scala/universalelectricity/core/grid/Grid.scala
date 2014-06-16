package universalelectricity.core.grid

import com.nicta.scoobi.impl.collection.WeakHashSet

/**
 * A grid containing a series of arbitary nodes.
 *
 * @param nodeClass - The type of the node.
 *
 * @author Calclavia
 */
class Grid[N](nodeClass: Class[_ <: N])
{
  /** A set of nodes in the grid (e.g conductors). */
  protected final val nodes = new WeakHashSet[N]()

  /**
   * Adds a node to the grid.
   * @param node
   */
  def add(node: N)
  {
    nodes.add(node)
  }

  /**
   * Removes a node to the grid.
   * @param node
   */
  def remove(node: N)
  {
    nodes.remove(node)
  }

  /**
   * This is READ ONLY! Make sure nodes are synchronized if modification is required.
   *
   * @return The list of nodes.
   */
  def getNodes() = nodes

  /**
   * Rebuilds the grid. The set "nodes" is copied due to the fact that this method will allow the modification of nodes while looping on a separate thread.
   */
  def reconstruct()
  {
    nodes.filter(isValidNode(_)).foreach(reconstructNode(_))

    //TODO: Check why it.remove is required
    /*
    val it: Iterator[N] = new HashSet[N](nodes)

    while (it.hasNext)
    {
      val node: N = it.next
      if (isValidNode(node))
      {
        reconstructNode(node)
      }
      else
      {
        it.remove
      }
    }*/
  }

  def isValidNode(node: Any): Boolean =
  {
    return nodeClass.isAssignableFrom(node.getClass)
  }

  protected def reconstructNode(node: N)
  {
  }

  /**
   * Clears all nodes
   */
  def deconstruct()
  {
    nodes.clear()
  }

  /**
   * Gets the first node in the node set. Note that the first node may be
   * random as this is a HashSet.
   *
   * @return The first node.
   */
  def getFirstNode(): N =
  {
    return nodes.head
  }

  override def toString: String =
  {
    return getClass.getSimpleName + "[" + hashCode + ", Nodes: " + nodes.size + "]"
  }
}