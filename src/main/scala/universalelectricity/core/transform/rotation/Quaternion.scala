package universalelectricity.core.transform.rotation

import java.math.{BigDecimal, MathContext, RoundingMode}

import universalelectricity.core.transform.ITransform
import universalelectricity.core.transform.vector.Vector3

/**
 * Quaternion class designed to be used for the rotation of objects.
 *
 * @author ChickenBones
 */
class Quaternion extends Cloneable with ITransform
{
  var x = 1D
  var y = 0D
  var z = 0D
  var w = 0D

  def this(Quaternion: Quaternion)
  {
    this()
    x = Quaternion.x
    y = Quaternion.y
    z = Quaternion.z
    w = Quaternion.w
  }

  def this(angle: Double, axis: Vector3)
  {
    this()
    setAroundAxis(axis.x, axis.y, axis.z, angle)
  }

  def this(d: Double, d1: Double, d2: Double, d3: Double)
  {
    this()
    x = d1
    y = d2
    z = d3
    w = d
  }

  def set(q: Quaternion): Quaternion =
  {
    x = q.x
    y = q.y
    z = q.z
    w = q.w
    return this
  }

  def set(d: Double, d1: Double, d2: Double, d3: Double): Quaternion =
  {
    x = d1
    y = d2
    z = d3
    w = d
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
    val d: Double = w * Quaternion.w - x * Quaternion.x - y * Quaternion.y - z * Quaternion.z
    val d1: Double = w * Quaternion.x + x * Quaternion.w - y * Quaternion.z + z * Quaternion.y
    val d2: Double = w * Quaternion.y + x * Quaternion.z + y * Quaternion.w - z * Quaternion.x
    val d3: Double = w * Quaternion.z - x * Quaternion.y + y * Quaternion.x + z * Quaternion.w
    w = d
    x = d1
    y = d2
    z = d3
    return this
  }

  def rightMultiply(Quaternion: Quaternion): Quaternion =
  {
    val d: Double = w * Quaternion.w - x * Quaternion.x - y * Quaternion.y - z * Quaternion.z
    val d1: Double = w * Quaternion.x + x * Quaternion.w + y * Quaternion.z - z * Quaternion.y
    val d2: Double = w * Quaternion.y - x * Quaternion.z + y * Quaternion.w + z * Quaternion.x
    val d3: Double = w * Quaternion.z + x * Quaternion.y - y * Quaternion.x + z * Quaternion.w
    w = d
    x = d1
    y = d2
    z = d3
    return this
  }

  def magnitude: Double =
  {
    return Math.sqrt(x * x + y * y + z * z + w * w)
  }

  def normalize: Quaternion =
  {
    var d: Double = magnitude
    if (d != 0)
    {
      d = 1 / d
      x *= d
      y *= d
      z *= d
      w *= d
    }
    return this
  }

  override def clone: Quaternion =
  {
    return new Quaternion(this)
  }

  /**
   * Rotates a vector
   * @param vector
   * @return
   */
  override def transform(vector: Vector3): Vector3 =
  {
    val d = -x * vector.x - y * vector.y - z * vector.z
    val d1 = w * vector.x + y * vector.z - z * vector.y
    val d2 = w * vector.y - x * vector.z + z * vector.x
    val d3 = w * vector.z + x * vector.y - y * vector.x
    return new Vector3(d1 * w - d * x - d2 * z + d3 * y, d2 * w - d * y + d1 * z - d3 * x, d3 * w - d * z - d1 * y + d2 * x)
  }

  override def toString: String =
  {
    val cont: MathContext = new MathContext(4, RoundingMode.HALF_UP)
    return "Quaternion[" + new BigDecimal(w, cont) + ", " + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + "]"
  }
}