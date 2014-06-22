package resonant.content.spatial

import resonant.content.component.IComponent

import scala.collection.mutable

/**
 * A spatial is anything that occupies space.
 *
 * TODO: Convert into class when passthrough interfaces are created.
 *
 * @author Calclavia
 */
trait Spatial
{
  val components = new mutable.LinkedList[IComponent]()
  {
    def getFirst[C](clazz: Class[C]): C =
    {
      return collectFirst(_.getClass().isAssignableFrom(clazz)).get.asInstanceOf[C]
    }

    def get[C](clazz: Class[C]): List[C] =
    {
      return filter(_.getClass().isAssignableFrom(clazz)).asInstanceOf[List[C]]
    }
  }

  /*
  var position: Vector3;
  var rotation: Rotation;
  */
}
