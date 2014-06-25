package universalelectricity.core.transform.rotation

import java.math.{BigDecimal, MathContext, RoundingMode}

import universalelectricity.core.transform.vector.Vector3

/**
 * Quaternion class designed to be used for the rotation of objects.
 *
 * @author ChickenBones
 */
object Quaternion
{
  def aroundAxis(ax: Double, ay: Double, az: Double, angle: Double): Quaternion =
  {
    return new Quaternion().setAroundAxis(ax, ay, az, angle)
  }

  def aroundAxis(axis: Vector3, angle: Double): Quaternion =
  {
    return aroundAxis(axis.x, axis.y, axis.z, angle)
  }
}

class Quaternion extends Cloneable
{
  var x = 1D
  var y = 0D
  var z = 0D
  var s = 0D

  def this(Quaternion: Quaternion)
  {
    this()
    x = Quaternion.x
    y = Quaternion.y
    z = Quaternion.z
    s = Quaternion.s
  }

  def this(d: Double, d1: Double, d2: Double, d3: Double)
  {
    this()
    x = d1
    y = d2
    z = d3
    s = d
  }

  def set(Quaternion: Quaternion): Quaternion =
  {
    x = Quaternion.x
    y = Quaternion.y
    z = Quaternion.z
    s = Quaternion.s
    return this
  }

  def set(d: Double, d1: Double, d2: Double, d3: Double): Quaternion =
  {
    x = d1
    y = d2
    z = d3
    s = d
    return this
  }

  def setAroundAxis(ax: Double, ay: Double, az: Double, originalAngle: Double): Quaternion =
  {
    var angle = originalAngle
    angle *= 0.5
    val d4: Double = Math.sin(angle)
    return set(Math.cos(angle), ax * d4, ay * d4, az * d4)
  }

  def setAroundAxis(axis: Vector3, angle: Double): Quaternion =
  {
    return setAroundAxis(axis.x, axis.y, axis.z, angle)
  }

  def multiply(Quaternion: Quaternion): Quaternion =
  {
    val d: Double = s * Quaternion.s - x * Quaternion.x - y * Quaternion.y - z * Quaternion.z
    val d1: Double = s * Quaternion.x + x * Quaternion.s - y * Quaternion.z + z * Quaternion.y
    val d2: Double = s * Quaternion.y + x * Quaternion.z + y * Quaternion.s - z * Quaternion.x
    val d3: Double = s * Quaternion.z - x * Quaternion.y + y * Quaternion.x + z * Quaternion.s
    s = d
    x = d1
    y = d2
    z = d3
    return this
  }

  def rightMultiply(Quaternion: Quaternion): Quaternion =
  {
    val d: Double = s * Quaternion.s - x * Quaternion.x - y * Quaternion.y - z * Quaternion.z
    val d1: Double = s * Quaternion.x + x * Quaternion.s + y * Quaternion.z - z * Quaternion.y
    val d2: Double = s * Quaternion.y - x * Quaternion.z + y * Quaternion.s + z * Quaternion.x
    val d3: Double = s * Quaternion.z + x * Quaternion.y - y * Quaternion.x + z * Quaternion.s
    s = d
    x = d1
    y = d2
    z = d3
    return this
  }

  def mag: Double =
  {
    return Math.sqrt(x * x + y * y + z * z + s * s)
  }

  def normalize: Quaternion =
  {
    var d: Double = mag
    if (d != 0)
    {
      d = 1 / d
      x *= d
      y *= d
      z *= d
      s *= d
    }
    return this
  }

  def copy: Quaternion =
  {
    return new Quaternion(this)
  }

  def rotate(vec: Vector3)
  {
    val d: Double = -x * vec.x - y * vec.y - z * vec.z
    val d1: Double = s * vec.x + y * vec.z - z * vec.y
    val d2: Double = s * vec.y - x * vec.z + z * vec.x
    val d3: Double = s * vec.z + x * vec.y - y * vec.x
    vec.x(d1 * s - d * x - d2 * z + d3 * y)
    vec.y(d2 * s - d * y + d1 * z - d3 * x)
    vec.z(d3 * s - d * z - d1 * y + d2 * x)
  }

  override def toString: String =
  {
    val cont: MathContext = new MathContext(4, RoundingMode.HALF_UP)
    return "Quaternion[" + new BigDecimal(s, cont) + ", " + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + "]"
  }

  def toRotation: Rotation = new Rotation(this)
}