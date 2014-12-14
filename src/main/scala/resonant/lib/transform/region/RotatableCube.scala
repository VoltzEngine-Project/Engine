package resonant.lib.transform.region

import net.minecraft.util.{AxisAlignedBB, Vec3}
import resonant.lib.transform.rotation.IRotation
import resonant.lib.transform.vector.IVector3

/**
 * Created by robert on 12/14/2014.
 */
class RotatableCube extends Cuboid with IRotation
{
  var yaw: Double = 0
  var pitch: Double = 0
  var roll : Double = 0

  override def intersects(v: Vec3): Boolean =
  {
    return isWithinX(v) && isWithinY(v) && isWithinZ(v)
  }

  override def intersects(v: IVector3): Boolean =
  {
    return isWithinX(v) && isWithinY(v) && isWithinZ(v)
  }

  override def doesOverlap(box: AxisAlignedBB): Boolean =
  {
    if(yaw.asInstanceOf[Int] != 0 || pitch.asInstanceOf[Int] != 0)
    {

    }
    return super.doesOverlap(box);
  }

  override def doesOverlap(box: Cuboid): Boolean =
  {
    if(yaw.asInstanceOf[Int] != 0 || pitch.asInstanceOf[Int] != 0)
    {

    }
    return true
  }
}