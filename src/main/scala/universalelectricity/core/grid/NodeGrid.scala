package universalelectricity.core.grid

import scala.collection.convert.wrapAll._
import scala.reflect.ClassTag

/**
 * A grid that consists of specific INodes
 *
 * @tparam N - The type of the node
 */
abstract class NodeGrid[N <: Node[N] : ClassTag] extends Grid[N]
{
  protected override def reconstructNode(node: N)
  {
    node.recache()
    node.setGrid(this)

    val connections = node.connections

    connections.keySet().foreach(connectedNode =>
    {
      //&& connection.isInstanceOf[Node]
      if (isValidNode(connectedNode))
      {
        if (connectedNode.getGrid != this)
        {
          connectedNode.getGrid.getNodes().clear()
          add(connectedNode)
          reconstructNode(connectedNode)
        }
      }
    })
  }

  override def deconstruct
  {
    getNodes().foreach(node =>
    {
      node.setGrid(null)
      node.reconstruct()
    })

    getNodes().clear()
  }
}