package resonant.engine.spatial

import universalelectricity.core.transform.vector.Vector3
import universalelectricity.core.transform.rotation.Rotation
import resonant.engine.component.IComponent
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
  val components: List[IComponent] = new mutable.LinkedList[IComponent]()
  {
    def getFirst(clazz: Class): IComponent =
    {
      return collectFirst(_.getClass().isAssignableFrom(clazz)).get
    }

    def get(clazz: Class): List[IComponent] =
    {
      return filter(_.getClass().isAssignableFrom(clazz))
    }
  }

  /*
  var position: Vector3;
  var rotation: Rotation;
  */
}
