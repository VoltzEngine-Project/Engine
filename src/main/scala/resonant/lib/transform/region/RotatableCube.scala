package resonant.lib.transform.region

import net.minecraft.util.AxisAlignedBB
import resonant.lib.transform.rotation.IRotation
import resonant.lib.transform.vector.{IVector3, Vector2}
import resonant.lib.utility.MathUtility

/**
 * Created by robert on 12/14/2014.
 */
class RotatableCube extends Cuboid with IRotation
{
  var yaw: Double = 0
  var pitch: Double = 0
  var roll: Double = 0

  override def intersects(x: Double, y: Double, z: Double): Boolean =
  {
    //Radius check to exclude by distance
    if (distance(x, y, z) > radius)
      return false;

    //Area base check xz
    val p = new Vector2(x, z)


    //Area face check xy
    p.y = y

    return true
  }

  override def isWithinXZ(pp: IVector3): Boolean =
  {
    return new Rectangle(new Vector2(min.x, min.z), new Vector2(max.x, max.z)).isWithin(new Vector2(pp.x, pp.y))
  }

  def isWithinYZ(pp: IVector3): Boolean =
  {
    val p = new Vector2(pp.y, pp.z)

    //Rect corners
    val a = new Vector2(min.y, min.z) // min
    val b = new Vector2(min.y, max.z)
    val c = new Vector2(max.y, max.z) // max
    val d = new Vector2(max.y, min.z)

    //Rectangle area
    val area = new Rectangle(a, c).area()

    //Area of the triangles made from the corners and p
    val areaAB = new Triangle(a, b, p).area()
    val areaBC = new Triangle(b, c, p).area()
    val areaCD = new Triangle(c, d, p).area()
    val areaDA = new Triangle(d, a, p).area()

    //If the area of the combined points is less and equals to area
    return (areaAB + areaBC + areaCD + areaDA) <= area
  }

  override def doesOverlap(box: AxisAlignedBB): Boolean =
  {
    if (yaw.asInstanceOf[Int] != 0 || pitch.asInstanceOf[Int] != 0)
    {
      return doesOverlap(new Cuboid(box))
    }
    return super.doesOverlap(box);
  }

  override def doesOverlap(box: Cuboid): Boolean =
  {
    if (yaw.asInstanceOf[Int] != 0 || pitch.asInstanceOf[Int] != 0)
    {
      //Radius check to quick exclude
      if (distance(box) - box.radius > radius)
        return false;

      //Corner check TODO replace with better check in case we get a zero corner overlap
      for (v <- Cuboid.getCorners(box))
      {
        if (intersects(v))
          return true
      }
      return false;
    }
    return true
  }

  def isRotated: Boolean =
  {
    return MathUtility.clampAngleTo180(yaw).asInstanceOf[Int] != 0 || MathUtility.clampAngleTo180(pitch).asInstanceOf[Int] != 0
  }

}