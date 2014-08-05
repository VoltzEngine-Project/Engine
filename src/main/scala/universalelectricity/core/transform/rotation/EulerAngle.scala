package universalelectricity.core.transform.rotation

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.core.transform.vector.Vector3
import universalelectricity.core.transform.{AbstractOperation, ITransform}

/**
 * The euler angles describing a 3D rotation. The rotation always in degrees.
 *
 * Note: The rotational system Minecraft uses is non-standard. The angles and vector calculations
 * have been calibrated to match. DEFINITIONS:
 *
 * Yaw: 0 Degrees - Looking at NORTH 90 - Looking at WEST 180 - Looking at SOUTH 270 - Looking at
 * EAST
 *
 * Pitch: 0 Degrees - Looking straight forward towards the horizon. 90 Degrees - Looking straight up
 * to the sky. -90 Degrees - Looking straight down to the void.
 *
 * Make sure all models use the Techne Model loader, they will naturally follow this rule.
 *
 * @author Calclavia
 */

object EulerAngle
{
  def angleDifference(angleA: Double, angleB: Double): Double =
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
    var result = value
    while (result < min) result += 360
    while (result > max) result -= 360
    return result
  }
}

class EulerAngle(var yaw: Double, var pitch: Double, var roll: Double) extends AbstractOperation[EulerAngle] with Cloneable with ITransform
{
  def this(yaw: Double, pitch: Double) = this(yaw, pitch, 0)

  def this(dir: ForgeDirection)
  {
    this(0, 0, 0)
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

  def set(yaw: Double, pitch: Double, roll: Double)
  {
    this.yaw = yaw
    this.pitch = pitch
    this.roll = roll
  }

  override def set(other: EulerAngle): EulerAngle =
  {
    yaw = other.yaw
    pitch = other.pitch
    roll = other.roll
    return this
  }

  /**
   * Operations
   */
  override def +(other: Double): EulerAngle = new EulerAngle(yaw + other, pitch + other, roll + other)

  override def +(other: EulerAngle): EulerAngle = new EulerAngle(yaw + other.yaw, pitch + other.pitch, roll + other.roll)

  override def *(other: Double): EulerAngle = new EulerAngle(yaw * other, pitch * other, roll * other)

  override def *(other: EulerAngle): EulerAngle = new EulerAngle(yaw * other.yaw, pitch * other.pitch, roll * other.roll)

  override def reciprocal() = new EulerAngle(1 / yaw, 1 / pitch, 1 / roll)

  def ceil = new EulerAngle(Math.ceil(yaw), Math.ceil(pitch), Math.ceil(roll))

  def floor = new EulerAngle(Math.floor(yaw), Math.floor(pitch), Math.floor(roll))

  def round = new EulerAngle(Math.round(yaw), Math.round(pitch), Math.round(roll))

  def max(other: EulerAngle) = new EulerAngle(Math.max(yaw, other.yaw), Math.max(pitch, other.pitch), Math.max(roll, other.roll))

  def min(other: EulerAngle) = new EulerAngle(Math.min(yaw, other.yaw), Math.min(pitch, other.pitch), Math.min(roll, other.roll))

  def absoluteDifference(other: EulerAngle) = new EulerAngle(EulerAngle.angleDifference(yaw, other.yaw), EulerAngle.angleDifference(pitch, other.pitch), EulerAngle.angleDifference(roll, other.roll))

  def isWithin(other: EulerAngle, margin: Double): Boolean = absoluteDifference(other).toTuple.productIterator.exists(i => i.asInstanceOf[Double] > margin)

  override def transform(vector: Vector3) = vector.transform(toQuaternion)

  def toVector: Vector3 = new Vector3(-Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch), -Math.cos(yaw) * Math.cos(pitch))

  def toAngleAxis: AngleAxis =
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

    val angle = 2 * Math.acos(w)
    var axis = new Vector3(x, y, z)

    if (axis.magnitudeSquared < 0.001)
    {
      axis = new Vector3(0, 0, -1)
    }
    else
    {
      axis = axis.normalize
    }

    return new AngleAxis(angle, axis)
  }

  def toQuaternion: Quaternion =
  {
    // Assuming the angles are in radians.
    val c1 = Math.cos(Math.toRadians(yaw) / 2)
    val s1 = Math.sin(Math.toRadians(yaw) / 2)
    val c2 = Math.cos(Math.toRadians(pitch) / 2)
    val s2 = Math.sin(Math.toRadians(pitch) / 2)
    val c3 = Math.cos(Math.toRadians(roll) / 2)
    val s3 = Math.sin(Math.toRadians(roll) / 2)
    val c1c2 = c1 * c2
    val s1s2 = s1 * s2
    val w = c1c2 * c3 - s1s2 * s3
    val x = c1c2 * s3 + s1s2 * c3
    val y = s1 * c2 * c3 + c1 * s2 * s3
    val z = c1 * s2 * c3 - s1 * c2 * s3
    return new Quaternion(w, x, y, z)
  }

  def toTuple: (Double, Double, Double) = (yaw, pitch, roll)

  override def clone() = new EulerAngle(yaw, pitch, roll)

  override def toString: String = "EulerAngle[" + yaw + "," + pitch + "," + roll + "]"

  override def writeByteBuf(data: ByteBuf) =
  {
    data.writeDouble(yaw)
    data.writeDouble(pitch)
    data.writeDouble(roll)
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("yaw", yaw)
    nbt.setDouble("pitch", pitch)
    nbt.setDouble("roll", roll)
    return nbt
  }
}
