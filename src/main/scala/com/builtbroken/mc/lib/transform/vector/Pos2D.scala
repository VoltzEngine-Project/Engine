package com.builtbroken.mc.lib.transform.vector

import java.lang.Double.doubleToLongBits
import com.builtbroken.jlib.data.IPos2D
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import com.builtbroken.mc.lib.transform.AbstractVector

/**
 * Vector2 Class is used for defining objects in a 2D space.
 *
 * @author Calclavia
 */
class Pos2D(var x: Double = 0, var y: Double =0 ) extends AbstractVector[Pos2D] with Ordered[Pos2D] with Cloneable with IPos2D
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

  override def set(vec: Pos2D): Pos2D =
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

  override def round: Pos2D = new Pos2D(Math.round(x), Math.round(y))

  override def ceil: Pos2D = new Pos2D(Math.ceil(x), Math.ceil(y))

  override def floor: Pos2D = new Pos2D(Math.floor(x), Math.floor(y))

  override def max(other: Pos2D): Pos2D = new Pos2D(Math.max(x, other.x), Math.max(y, other.y))

  override def min(other: Pos2D): Pos2D = new Pos2D(Math.min(x, other.x), Math.min(y, other.y))

  override def clone: Pos2D = new Pos2D(this.x, this.y)

  override def reciprocal: Pos2D = new Pos2D(1 / x, 1 / y)

  /**
   * Operations
   */
  override def +(amount: Double): Pos2D = new Pos2D(x + amount, y + amount)

  override def +(amount: Pos2D): Pos2D = new Pos2D(x + amount.x, y + amount.y)

  def +(x: Double, y: Double): Pos2D = new Pos2D(this.x + x, this.y + y)

  def +=(x: Double, y: Double): Pos2D = set(new Pos2D(this.x + x, this.y + y))

  def add(x: Double, y: Double): Pos2D = this +(x, y)

  def addEquals(x: Double, y: Double): Pos2D = this +=(x, y)

  override def *(amount: Double): Pos2D = new Pos2D(x * amount, y * amount)

  override def *(amount: Pos2D): Pos2D = new Pos2D(x * amount.x, y * amount.y)

  /**
   * Rotates this vector by an angle.
   * @param angle Angle in radians.
   * @return The rotated vector.
   */
  def rotate(angle: Double) = new Pos2D(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle))

  /**
   * Magnitude
   */
  override def $(other: Pos2D) = x * other.x + y * other.y

  def slope(other: Pos2D) = (y - other.y) / (x - other.x)

  override def hashCode: Int =
  {
    val x = doubleToLongBits(this.x)
    val y = doubleToLongBits(this.y)
    return 31 * (x ^ (x >>> 32)).toInt + (y ^ (y >>> 32)).toInt
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[Pos2D])
    {
      val vector: Pos2D = o.asInstanceOf[Pos2D]
      return this.x == vector.x && this.y == vector.y
    }
    return false
  }

  override def compare(that: Pos2D): Int =
  {
    if (x < that.x || y < that.y)
      return -1

    if (x > that.x || y > that.y)
      return 1

    return 0
  }

  override def toString = "Vector2 [" + this.x + "," + this.y + "]"
}