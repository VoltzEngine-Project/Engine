package universalelectricity.core.grid

import scala.collection.convert.wrapAll._
/**
 * A grid that consists of specific INodes
 *
 * @tparam N - The type of the node
 */
abstract class NodeGrid[N <: Node] extends Grid[N]
{
  protected override def reconstructNode(node: N)
  {
    node.recache()
    node.setGrid(this)

    val connections = node.getConnections()

    connections.keySet().foreach(connectedNode =>
    {
      //&& connection.isInstanceOf[Node]
      if (isValidNode(connectedNode))
      {
        if (connectedNode.getGrid() != this)
        {
          connectedNode.getGrid().getNodes().clear()
          add(connectedNode)
          reconstructNode(connectedNode.asInstanceOf[N])
        }
      }
    })
  }

  override def deconstruct
  {
    nodes.foreach(node =>
    {
      node.setGrid(null)
      node.reconstruct()
    })

    nodes.clear
  }
}