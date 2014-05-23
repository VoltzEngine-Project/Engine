package universalelectricity.core.vector

import net.minecraftforge.common.util.ForgeDirection

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
 * Make sure all models are use the Techne Model loader, they will naturally follow this rule.
 *
 * @author Calclavia
 */
class Rotation extends ITransform
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
    this()
    `this`(angle, new Vector3(x, y, z))
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

  def this(dir: ForgeDirection)
  {
    this()
    dir match
    {
      case DOWN =>
        pitch = -90
        break //todo: break is not supported
      case UP =>
        pitch = 90
        break //todo: break is not supported
      case NORTH =>
        yaw = 0
        break //todo: break is not supported
      case SOUTH =>
        yaw = 180
        break //todo: break is not supported
      case WEST =>
        yaw = 90
        break //todo: break is not supported
      case EAST =>
        yaw = -90
        break //todo: break is not supported
      case _ =>
        break //todo: break is not supported
    }
  }

  def this(yaw: Double, pitch: Double, roll: Double)
  {
    this()
    this.yaw = yaw
    this.pitch = pitch
    this.roll = roll
  }

  def angle(radians: Double)
  {
    angle = radians
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

  def transform(vector: Vector3)
  {
    val quat: Quaternion = Quaternion.aroundAxis(axis, angle)
    quat.rotate(vector)
  }

  /**
   * Angles in degrees.
   */
  override def hashCode: Int =
  {
    val angle: Long = Double.doubleToLongBits(this.angle)
    var hash: Int = axis.hashCode
    hash = 31 * hash + (angle ^ (angle >>> 32)).asInstanceOf[Int]
    return hash
  }

  override def equals(o: AnyRef): Boolean =
  {
    if (o.isInstanceOf[Rotation])
    {
      val other: Rotation = o.asInstanceOf[Rotation]
      return (axis == other.axis) && angle == other.angle
    }
    return false
  }

  override def toString: String =
  {
    return "Angle [" + this.yaw + "," + this.pitch + "," + this.roll + "]"
  }

}