package com.builtbroken.mc.lib.transform.rotation

import java.lang.Double._
import com.builtbroken.jlib.data.vector.IPos3D
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import com.builtbroken.mc.lib.transform.ITransform
import com.builtbroken.mc.lib.transform.matrix.Matrix
import com.builtbroken.mc.lib.transform.vector.Pos

/**
 * A defined angle axis rotation. Recommend to use Quaternions as they are generally faster.
 *
 * angle - An angle in radians
 * axis - A unit vector to rotate around
 * @author Calclavia
 */
class AngleAxis(var angle: Double = 0D, var axis: Pos = new Pos) extends Cloneable with ITransform
{
  def this(nbt: NBTTagCompound)
  {
    this(nbt.getDouble("angle"), new Pos(nbt))
  }

  def ceil = new AngleAxis(Math.ceil(angle), axis.ceil)

  def floor = new AngleAxis(Math.floor(angle), axis.floor)

  def round = new AngleAxis(Math.round(angle), axis.round)

  /**
   * TODO: Alternatively we can transform vai quaternion.
   */
  override def transform(vector: IPos3D) = toRotationMatrix * vector

  /**
   * Creates a rotation matrix for an angle-axis rotation around this vector.
   * @return - A 3x3 rotation matrix
   */
  def toRotationMatrix: Matrix =
  {
    val x = axis.x
    val y = axis.y
    val z = axis.z

    /**
     * Predefine trigonometric calculation to save computation time.
     */
    val cos = Math.cos(angle)
    val oneMinusCos = 1 - cos
    val sin = Math.sin(angle)

    /**
     * Creates a 3x3 matrix using Rodrigues' rotation formula.
     */
    val matrix = new Matrix(3, 3)
    matrix(0)(0) = x * x * oneMinusCos + cos
    matrix(0)(1) = y * x * oneMinusCos + z * sin
    matrix(0)(2) = x * z * oneMinusCos - y * sin
    matrix(1)(0) = x * y * oneMinusCos - z * sin
    matrix(1)(1) = y * y * oneMinusCos + cos
    matrix(1)(2) = y * z * oneMinusCos + x * sin
    matrix(2)(0) = x * z * oneMinusCos + y * sin
    matrix(2)(1) = y * z * oneMinusCos - x * sin
    matrix(2)(2) = z * z * oneMinusCos + cos
    return matrix
  }

  def toEulerAngle: EulerAngle =
  {
    val x = axis.x
    val y = axis.y
    val z = axis.z

    val s: Double = Math.sin(angle)
    val c: Double = Math.cos(angle)
    val t: Double = 1 - c

    var yaw = 0D
    var pitch = 0D
    var roll = 0D

    if ((x * y * t + z * s) > 0.998)
    {
      yaw = 2 * Math.atan2(x * Math.sin(angle / 2), Math.cos(angle / 2))
      pitch = Math.PI / 2
      roll = 0
      return new EulerAngle(yaw, pitch, roll)
    }

    if ((x * y * t + z * s) < -0.998)
    {
      yaw = -2 * Math.atan2(x * Math.sin(angle / 2), Math.cos(angle / 2))
      pitch = -Math.PI / 2
      roll = 0
      return new EulerAngle(yaw, pitch, roll)
    }

    yaw = Math.atan2(y * s - x * z * t, 1 - (y * y + z * z) * t)
    pitch = Math.asin(x * y * t + z * s)
    roll = Math.atan2(x * s - y * z * t, 1 - (x * x + z * z) * t)
    return new EulerAngle(yaw, pitch, roll)
  }

  def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    axis.writeNBT(nbt)
    nbt.setDouble("angle", angle)
    return nbt
  }

  def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    axis.writeByteBuf(data)
    data.writeDouble(angle)
    return data
  }

  override def clone: AngleAxis = new AngleAxis(angle, axis)

  override def hashCode: Int =
  {
    val angle = doubleToLongBits(this.angle)
    var hash = axis.hashCode
    hash = 31 * hash + (angle ^ (angle >>> 32)).asInstanceOf[Int]
    return hash
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[AngleAxis])
    {
      val other = o.asInstanceOf[AngleAxis]
      return axis.equals(other.axis) && angle == other.angle
    }

    return false
  }
}
