package universalelectricity.core.vector

import java.lang.Double.doubleToLongBits
import net.minecraft.nbt.NBTTagCompound

/**
 * Vector2 Class is used for defining objects in a 2D space.
 *
 * @author Calclavia
 */
class Vector2 extends Cloneable
{
  var x = 0D
  var y = 0D

  def this(x: Double, y: Double)
  {
    this()
    this.x = x
    this.y = y
  }

  def x(amount: Double)
  {
    x = amount
  }

  def y(amount: Double)
  {
    y = amount
  }

  def xi = x.toInt

  def yi = y.toInt

  def xf = x.toFloat

  def yf = y.toFloat

  def round: Vector2 = new Vector2(Math.round(x), Math.round(y))

  def ceil: Vector2 = new Vector2(Math.ceil(x), Math.ceil(y))

  def floor: Vector2 = new Vector2(Math.floor(x), Math.floor(y))

  def max(other: Vector2): Vector2 = new Vector2(Math.max(x, other.x), Math.max(y, other.y))

  def min(other: Vector2): Vector2 = new Vector2(Math.min(x, other.x), Math.min(y, other.y))

  override def clone: Vector2 = return new Vector2(this.x, this.y)

  /**
   * Operations
   */
  def +(amount: Double): Vector2 = new Vector2(x + amount, y + amount)

  def +(amount: Vector2): Vector2 = new Vector2(x + amount.x, y + amount.y)

  def -(amount: Double): Vector2 = this + -amount

  def -(amount: Vector2): Vector2 = this + (amount * -1)

  def *(amount: Double): Vector2 = new Vector2(x * amount, y * amount)

  def /(amount: Double): Vector2 = new Vector2(x / amount, y / amount)

  def +=(amount: Double): Vector2 =
  {
    x += amount
    y += amount
    return this
  }

  def +=(amount: Vector2): Vector2 =
  {
    x += amount.x
    y += amount.y
    return this
  }

  def -=(amount: Double): Vector2 =
  {
    this += -amount
    return this
  }

  def -=(amount: Vector2): Vector2 =
  {
    this += (amount * -1)
    return this
  }

  def *=(amount: Double): Vector2 =
  {
    x *= amount
    y *= amount
    return this
  }

  def *=(amount: Vector2): Vector2 =
  {
    x *= amount.x
    y *= amount.y
    return this
  }

  def /=(amount: Double): Vector2 =
  {
    this *= (1 / amount)
    return this
  }


  def /=(amount: Vector2): Vector2 =
  {
    x /= amount.x
    y /= amount.y
    return this
  }

  /**
   * Magnitude
   */
  def dot(other: Vector2) = $(other)

  def $(other: Vector2) = x * other.x + y * other.y

  def magnitudeSquared = this $ this;

  def magnitude = Math.sqrt(magnitudeSquared)

  def normalize = this / magnitude

  def distance(other: Vector3) = (other - this).magnitude

  def midpoint(pos: Vector2): Vector2 = new Vector2((x + pos.x) / 2, (y + pos.y) / 2)

  def slope(other: Vector2) = (y - other.y) / (x - other.x)

  def writeToNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    return nbt
  }

  override def hashCode: Int =
  {
    val x = doubleToLongBits(this.x)
    val y = doubleToLongBits(this.y)
    return 31 * (x ^ (x >>> 32)).toInt + (y ^ (y >>> 32)).toInt
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[Vector2])
    {
      val vector: Vector2 = o.asInstanceOf[Vector2]
      return this.x == vector.x && this.y == vector.y
    }
    return false
  }

  override def toString = "Vector2 [" + this.x + "," + this.y + "]"
}