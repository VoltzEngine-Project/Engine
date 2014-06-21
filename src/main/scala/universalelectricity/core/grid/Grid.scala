package universalelectricity.core.grid

import com.nicta.scoobi.impl.collection.WeakHashSet
import scala.reflect._
import scala.reflect.ClassTag

/**
 * A grid containing a series of arbitary nodes.
 *
 * @author Calclavia
 */
class Grid[N]
{
	/** A set of nodes in the grid (e.g conductors). */
	private final val nodes = new WeakHashSet[N]()

	/** A class instance of the node type used to node validation checks. */
	var nodeClass : Class[N] = _

	/**
	 * Adds a node to the grid.
	 * @param node
	 */
	def add(node: N)
	{
		nodes.add(node)

    //TODO: Check this?
   nodeClass = classOf[node]
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
	}

	def isValidNode(node: Any): Boolean =
	{
		//TODO: Check if better way to do this.
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