package resonant.lib.transform.vector

import java.lang.Double.doubleToLongBits

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import resonant.lib.transform.AbstractVector

/**
 * Vector2 Class is used for defining objects in a 2D space.
 *
 * @author Calclavia
 */
class Vector2(var x: Double = 0, var y: Double =0 ) extends AbstractVector[Vector2] with Ordered[Vector2] with Cloneable with IVector2
{
  def this() = this(0, 0)

  def this(value: Double) = this(value, value)

  def this(data: ByteBuf) = this(data.readDouble(), data.readDouble())

  def x(amount: Double)
  {
    x = amount
  }

  def y(amount: Double)
  {
    y = amount
  }

  override def set(vec: Vector2): Vector2 =
  {
    x = vec.x
    y = vec.y
    return this
  }

  /**
   * Conversion
   */
  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    return nbt
  }

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data.writeDouble(x)
    data.writeDouble(y)
    return data
  }

  def xi = x.toInt

  def yi = y.toInt

  def xf = x.toFloat

  def yf = y.toFloat

  override def round: Vector2 = new Vector2(Math.round(x), Math.round(y))

  override def ceil: Vector2 = new Vector2(Math.ceil(x), Math.ceil(y))

  override def floor: Vector2 = new Vector2(Math.floor(x), Math.floor(y))

  override def max(other: Vector2): Vector2 = new Vector2(Math.max(x, other.x), Math.max(y, other.y))

  override def min(other: Vector2): Vector2 = new Vector2(Math.min(x, other.x), Math.min(y, other.y))

  override def clone: Vector2 = return new Vector2(this.x, this.y)

  override def reciprocal: Vector2 = new Vector2(1 / x, 1 / y)

  /**
   * Operations
   */
  override def +(amount: Double): Vector2 = new Vector2(x + amount, y + amount)

  override def +(amount: Vector2): Vector2 = new Vector2(x + amount.x, y + amount.y)

  def +(x: Double, y: Double): Vector2 = new Vector2(this.x + x, this.y + y)

  def +=(x: Double, y: Double): Vector2 = set(new Vector2(this.x + x, this.y + y))

  def add(x: Double, y: Double): Vector2 = this +(x, y)

  def addEquals(x: Double, y: Double): Vector2 = this +=(x, y)

  override def *(amount: Double): Vector2 = new Vector2(x * amount, y * amount)

  override def *(amount: Vector2): Vector2 = new Vector2(x * amount.x, y * amount.y)

  /**
   * Rotates this vector by an angle.
   * @param angle Angle in radians.
   * @return The rotated vector.
   */
  def rotate(angle: Double) = new Vector2(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle))

  /**
   * Magnitude
   */
  override def $(other: Vector2) = x * other.x + y * other.y

  def slope(other: Vector2) = (y - other.y) / (x - other.x)

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

  override def compare(that: Vector2): Int =
  {
    if (x < that.x || y < that.y)
      return -1

    if (x > that.x || y > that.y)
      return 1

    return 0
  }

  override def toString = "Vector2 [" + this.x + "," + this.y + "]"
}