package universalelectricity.core.vector

import net.minecraft.nbt.NBTTagCompound

/**
 * @author Calclavia
 */
trait TraitVector[T <: TraitVector[T]]
{
  /**
   * Operations
   */
  def +(amount: Double): T

  def +(amount: T): T

  final def -(amount: Double): T = this + -amount

  final def -(amount: T): T = this + (amount * -1)

  def *(amount: Double): T

  final def /(amount: Double): T = this * (1 / amount)

  final def +=(amount: Double): T = set(this + amount)

  final def +=(amount: T): T = set(amount)

  final def -=(amount: Double): T = this += -amount

  final def -=(amount: T): T = this += (amount * -1)

  final def *=(amount: Double): T = set(this * amount)

  final def /=(amount: Double): T = this *= (1 / amount)

  /**
   * Alias Operation Methods
   */
  final def add(amount: Double): T = this + amount

  final def add(amount: T): T = this + amount

  final def subtract(amount: Double): T = this - amount

  final def subtract(amount: T): T = this - amount

  final def multiply(amount: Double): T = this * amount

  final def divide(amount: Double): T = this / amount

  final def addSet(amount: Double): T = this += amount

  final def addSet(amount: T): T = this += amount

  final def subtractSet(amount: Double): T = this -= amount

  final def subtractSet(amount: T): T = this -= amount

  final def multiplySet(amount: Double): T = this *= amount

  final def divideSet(amount: Double): T = this /= amount

  /**
   * Conversion
   */
  def round: T

  def ceil: T

  def floor: T

  def max(other: T): T

  def min(other: T): T

  def reciprocal(): T

  final def toNBT: NBTTagCompound = toNBT(new NBTTagCompound())

  def toNBT(nbt: NBTTagCompound): NBTTagCompound

  /**
   * Magnitudes
   */
  final def dot(other: T) = $(other)

  def $(other: T): Double

  final def magnitudeSquared: Double = this.asInstanceOf[T] $ this.asInstanceOf[T];

  final def magnitude = Math.sqrt(magnitudeSquared)

  final def normalize = this / magnitude

  final def distance(other: T) = (other - this.asInstanceOf[T]).magnitude

  final def midpoint(other: T): T = (this + other) / 2


  /**
   * Misc
   */
  def set(other: T): T
}
