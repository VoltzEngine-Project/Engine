package com.builtbroken.mc.lib.transform.region

import com.builtbroken.mc.lib.helper.MathUtility
import com.builtbroken.mc.lib.transform.vector.Point
import net.minecraft.util.AxisAlignedBB

/**
 * Created by robert on 12/14/2014.
 */
@Deprecated
class RotatableCube extends Cube
{

  override def intersects(x: Double, y: Double, z: Double): Boolean =
  {
    //Radius check to exclude by distance
    if (distance(x, y, z) > radius)
      return false;

    return isWithinXZ(x, y, z) && isWithinYZ(x, y, z)
  }

  def isWithinXZ(x: Double, y: Double, z: Double): Boolean =
  {
    return new Rectangle(new Point(min.x, min.z), new Point(max.x, max.z)).isWithin(x, z)
  }

  def isWithinYZ(x: Double, y: Double, z: Double): Boolean =
  {
    return new Rectangle(new Point(min.y, min.z), new Point(max.y, max.z)).isWithin(y, z)
  }

  override def doesOverlap(box: AxisAlignedBB): Boolean =
  {
    if (yaw.asInstanceOf[Int] != 0 || pitch.asInstanceOf[Int] != 0)
    {
      return doesOverlap(new Cube(box))
    }
    return super.doesOverlap(box);
  }

  override def doesOverlap(box: Cube): Boolean =
  {
    if (yaw.asInstanceOf[Int] != 0 || pitch.asInstanceOf[Int] != 0)
    {
      //Radius check to quick exclude
      if (distance(box) - box.radius > radius)
        return false;

      //Corner check TODO replace with better check in case we get a zero corner overlap
      for (v <- Cube.getCorners(box))
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