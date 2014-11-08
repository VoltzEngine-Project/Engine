package resonant.lib.grid.node

import resonant.api.grid.{INode, INodeProvider}
import resonant.lib.transform.vector.{TVectorWorld, VectorWorld}

/**
 * A node is any single part of a grid.
 * @author Calclavia
 */
abstract class Node(var parent: INodeProvider) extends INode with TVectorWorld
{
  override def reconstruct()
  {
  }

  override def deconstruct()
  {
  }

  override def getParent: INodeProvider = parent

  /** @deprecated use asVectorWorld instead */
  @Deprecated
  def position: VectorWorld = new VectorWorld(this)

  override def toString: String = getClass.getSimpleName + "[" + hashCode + "]"
}