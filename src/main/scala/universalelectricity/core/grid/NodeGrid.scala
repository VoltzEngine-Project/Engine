package universalelectricity.core.grid

import scala.collection.convert.wrapAll._
import scala.reflect.ClassTag

/**
 * A grid that consists of specific INodes
 *
 * @tparam N - The type of the node
 */
abstract class NodeGrid[N <: Node[_] : ClassTag] extends Grid[N]
{
  protected override def reconstructNode(node: N)
  {
    node.recache()
    node.setGrid(this)

    val connections = node.connections

    connections.keySet().foreach(
      connectedNode =>
      {
        if (isValidNode(connectedNode))
        {
          val con = connectedNode.asInstanceOf[N]

          if (con.getGrid != this)
          {
            con.getGrid.getNodes().clear()
            add(con)
            reconstructNode(con)
          }
        }
      })
  }

  override def deconstruct
  {
    getNodes().foreach(
      node =>
      {
        node.setGrid(null)
        node.reconstruct()
      })

    getNodes().clear()
  }
}