package com.builtbroken.lib.transform

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound

/**
 * An abstract class that manages arithmetic operations.
 * @author Calclavia
 */
abstract class AbstractOperation[T <: AbstractOperation[T]]
{
  def set(other: T): T

  /**
   * Operations
   */

  /**
   * Returns this value, unmodified.
   */
  def unary_+ : T = this.asInstanceOf[T]

  /**
   * Returns the negation of this value.
   */
  def unary_- : T = this * -1

  def +(amount: Double): T

  def +(amount: T): T

  def *(amount: Double): T

  def *(amount: T): T

  /**
   * Automatically generated
   */
  def -(amount: Double): T = this + -amount

  def -(amount: T): T = this + (-amount)

  def /(amount: Double): T = this * (1 / amount)

  def /(amount: T): T = this * (amount * -1)

  def +=(amount: Double): T = set(this + amount)

  def +=(amount: T): T = set(this + amount)

  def -=(amount: Double): T = this += -amount

  def -=(amount: T): T = this += (-amount)

  def *=(amount: Double): T = set(this * amount)

  def *=(amount: T): T = set(this * amount)

  def /=(amount: Double): T = this *= (1 / amount)

  def /=(amount: T): T = this *= amount.reciprocal

  /**
   * Alias Operation Methods
   */
  def add(amount: Double): T = this + amount

  def add(amount: T): T = this + amount

  def subtract(amount: Double): T = this - amount

  def subtract(amount: T): T = this - amount

  def multiply(amount: Double): T = this * amount

  def multiply(amount: T): T = this * amount

  def divide(amount: Double): T = this / amount

  def addEquals(amount: Double): T = this += amount

  def addEquals(amount: T): T = this += amount

  def subtractEquals(amount: Double): T = this -= amount

  def subtractEquals(amount: T): T = this -= amount

  def multiplyEquals(amount: Double): T = this *= amount

  def multiplyEquals(amount: T): T = this *= amount

  def divideEquals(amount: Double): T = this /= amount

  def divideEquals(amount: T): T = this /= amount

  /**
   * Conversion
   */
  def round: T

  def ceil: T

  def floor: T

  def max(other: T): T

  def min(other: T): T

  def reciprocal(): T

  final def toNBT: NBTTagCompound = writeNBT(new NBTTagCompound())

  def writeNBT(nbt: NBTTagCompound): NBTTagCompound

  def writeByteBuf(data: ByteBuf): ByteBuf
}
