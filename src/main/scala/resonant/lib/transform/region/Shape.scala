package resonant.lib.transform.region

import net.minecraft.util.Vec3
import resonant.lib.transform.AbstractOperation
import resonant.lib.transform.vector.{IVector2, IVector3, Vector2, Vector3}

/** Prefab for shapes
  * Created by robert on 12/17/2014.
  */
abstract class Shape[T <: Shape[T]] extends AbstractOperation[T]
{
  /** Gets the area of the shape */
  def getArea: Double

  def isWithin2D(vec: IVector2): Boolean

  def isWithin(x: Double, y: Double): Boolean =
  {
    return isWithin2D(new Vector2(x, y))
  }

  def isWithin(vec: IVector3): Boolean =
  {
    return isWithin2D(vec)
  }

  def isWithin(vec: Vec3): Boolean =
  {
    return isWithin(vec.xCoord, vec.yCoord, vec.zCoord)
  }

  def isWithin(x: Double, y: Double, z: Double): Boolean =
  {
    return isWithin(new Vector3(x, y, z))
  }

  override def round: T = this.asInstanceOf[T]

  override def reciprocal(): T = this.asInstanceOf[T]

  override def max(other: T): T = this.asInstanceOf[T]

  override def floor: T = this.asInstanceOf[T]

  override def min(other: T): T = this.asInstanceOf[T]

  override def ceil: T = this.asInstanceOf[T]
}