package universalelectricity.core.vector

import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.nbt.NBTTagCompound
import java.lang.Double.doubleToLongBits

/**
 * The euler angles describing a 3D rotation. The rotation always in degrees.
 * <p/>
 * Note: The rotational system Minecraft uses is non-standard. The angles and vector calculations
 * have been calibrated to match. DEFINITIONS:
 * <p/>
 * Yaw: 0 Degrees - Looking at NORTH 90 - Looking at WEST 180 - Looking at SOUTH 270 - Looking at
 * EAST
 * <p/>
 * Pitch: 0 Degrees - Looking straight forward towards the horizon. 90 Degrees - Looking straight up
 * to the sky. -90 Degrees - Looking straight down to the void.
 * <p/>
 * Make sure all models use the Techne Model loader, they will naturally follow this rule.
 *
 * @author Calclavia
 */
class Rotation extends ITransform with IRotation
{
  /**
   * An angle in radians
   */
  var angle = 0D

  /**
   * A normalized axis
   */
  var axis: Vector3 = new Vector3()

  def this(angle: Double, axis: Vector3)
  {
    this()
    this.angle = angle
    this.axis = axis
  }

  def this(angle: Double, x: Double, y: Double, z: Double)
  {
    this(angle, new Vector3(x, y, z))
  }

  def this(quat: Quaternion)
  {
    this()
    angle = Math.acos(quat.s) * 2
    if (angle == 0)
    {
      axis = new Vector3(0, 1, 0)
    }
    else
    {
      val sa: Double = Math.sin(angle * 0.5)
      axis = new Vector3(quat.x / sa, quat.y / sa, quat.z / sa)
    }
  }

  def this(yaw: Double, pitch: Double, roll: Double)
  {
    this()
    set(yaw, pitch, roll)
  }

  def this(nbt: NBTTagCompound)
  {
    this()
    axis = new Vector3(nbt)
    angle = nbt.getDouble("angle")
  }

  def this(dir: ForgeDirection)
  {
    this()
    dir match
    {
      case ForgeDirection.DOWN => set(0, -90, 0)
      case ForgeDirection.UP => set(0, 90, 0)
      case ForgeDirection.NORTH => set(0, 0, 0)
      case ForgeDirection.SOUTH => set(180, 0, 0)
      case ForgeDirection.WEST => set(90, 0, 0)
      case ForgeDirection.EAST => set(-90, 0, 0)
      case _ =>
    }
  }

  def angle(radians: Double)
  {
    angle = radians
  }

  /**
   * Sets the euler angles. Angles are in radians.
   */
  def set(yaw: Double, pitch: Double, roll: Double)
  {
    val c1 = Math.cos(yaw / 2)
    val s1 = Math.sin(yaw / 2)
    val c2 = Math.cos(pitch / 2)
    val s2 = Math.sin(pitch / 2)
    val c3 = Math.cos(roll / 2)
    val s3 = Math.sin(roll / 2)
    val c1c2 = c1 * c2
    val s1s2 = s1 * s2
    val w = c1c2 * c3 - s1s2 * s3
    val x = c1c2 * s3 + s1s2 * c3
    val y = s1 * c2 * c3 + c1 * s2 * s3
    val z = c1 * s2 * c3 - s1 * c2 * s3
    angle = 2 * Math.acos(w)
    axis = new Vector3(x, y, z)

    if (axis.magnitudeSquared < 0.001)
    {
      axis = new Vector3(0, 0, -1)
    }
    else
    {
      axis = axis.normalize
    }
  }

  /**
   * Conversions
   */
  def toEuler(): Tuple3[Double, Double, Double] =
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
      return new Tuple3(yaw, pitch, roll)
    }

    if ((x * y * t + z * s) < -0.998)
    {
      yaw = -2 * Math.atan2(x * Math.sin(angle / 2), Math.cos(angle / 2))
      pitch = -Math.PI / 2
      roll = 0
      return new Tuple3(yaw, pitch, roll)
    }

    yaw = Math.atan2(y * s - x * z * t, 1 - (y * y + z * z) * t)
    pitch = Math.asin(x * y * t + z * s)
    roll = Math.atan2(x * s - y * z * t, 1 - (x * x + z * z) * t)
    return new Tuple3(yaw, pitch, roll)
  }

  def toVector(): Vector3 = new Vector3(-Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch), -Math.cos(yaw) * Math.cos(pitch))

  def toNBT(): NBTTagCompound = toNBT(new NBTTagCompound())

  def toNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    axis.toNBT(nbt)
    nbt.setDouble("angle", angle)
    return nbt
  }

  def yaw: Double =
  {
    return toEuler()._1
  }

  def pitch: Double =
  {
    return toEuler()._2
  }

  def roll: Double =
  {
    return toEuler()._3
  }

  /** Gets the difference in degrees between the two angles */
  def difference(other: Rotation): Rotation = this - other

  def -(other: Rotation): Rotation = new Rotation(yaw - other.yaw, pitch - other.pitch, roll - other.roll)

  def absoluteDifference(other: Rotation): Rotation =
  {
    return new Rotation(getAngleDifference(yaw, other.yaw), getAngleDifference(pitch, other.pitch), getAngleDifference(roll, other.roll))
  }

  def isWithin(other: Rotation, margin: Double): Boolean =
  {
    var i: Int = 0

    val difference = absoluteDifference(other).toEuler()

    difference.productIterator.find(
      i =>
      {
        if (i > margin) return false
      }
    )

    return true
  }

  def getAngleDifference(angleA: Double, angleB: Double): Double =
  {
    return Math.abs(angleA - angleB)
  }

  def clampAngleTo360(value: Double): Double =
  {
    return clampAngle(value, -360, 360)
  }

  def clampAngleTo180(value: Double): Double =
  {
    return clampAngle(value, -180, 180)
  }

  def clampAngle(value: Double, min: Double, max: Double): Double =
  {
    while (value < min) value += 360
    while (value > max) value -= 360
    return value
  }

  def transform(vector: Vector3)
  {
    val quat: Quaternion = Quaternion.aroundAxis(axis, angle)
    quat.rotate(vector)
  }

  override def clone: Rotation =
  {
    return new Rotation(angle, axis)
  }

  override def hashCode: Int =
  {
    val angle = doubleToLongBits(this.angle)
    var hash = axis.hashCode
    hash = 31 * hash + (angle ^ (angle >>> 32)).asInstanceOf[Int]
    return hash
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[Rotation])
    {
      val other: Rotation = o.asInstanceOf[Rotation]
      return (axis == other.axis) && angle == other.angle
    }
    return false
  }

  override def toString: String = "Rotation[" + Math.toDegrees(yaw) + "," + Math.toDegrees(pitch) + "," + Math.toDegrees(roll) + "]"
}