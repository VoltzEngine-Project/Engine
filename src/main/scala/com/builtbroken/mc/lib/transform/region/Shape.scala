package com.builtbroken.mc.lib.transform.region

import com.builtbroken.jlib.data.{IPos3D, IPos2D}
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3
import com.builtbroken.mc.lib.transform.AbstractOperation
import com.builtbroken.mc.lib.helper.wrapper.ByteBufWrapper._

/** Prefab for 2D shapes, assumes that if the shape is placed into the world that
  * the shape is on the horizontal. In order to change this the shape will need
  * to be recreated as the 3D version with one of the sizes converted to zero.
  *
  * Created by robert on 12/17/2014.
  */
abstract class Shape[T <: Shape[T]] extends AbstractOperation[T]
{
  /** Rotation around the Y */
  var yaw: Double = 0;

  /** Distance the shape takes in the X axis */
  def getSizeX: Double

  /** Distance the shape takes in the Y axis */
  def getSizeY: Double

  /** Gets the area of the shape */
  def getArea: Double

  //====================
  // Collision check methods
  //====================

  /** Checks if the point is inside the shape */
  def isWithin2D(vec: IPos2D): Boolean = isWithin(vec.x, vec.y)

  /** Checks if the point is inside the shape */
  def isWithin(vec: IPos3D): Boolean = isWithin(vec.x, vec.y, vec.z)

  /** Checks if the point is inside the shape */
  def isWithin(vec: Vec3): Boolean = isWithin(vec.xCoord, vec.yCoord, vec.zCoord)

  /** Checks if the point is inside the shape */
  def isWithin(x: Double, y: Double): Boolean

  /** Checks if the point is inside the shape */
  def isWithin(x: Double, y: Double, z: Double): Boolean = isWithin(x, z)

  //=========================
  // Math operators that can't be easily support
  // so they return with no change
  //=========================
  override def round: T = this.asInstanceOf[T]

  override def reciprocal(): T = this.asInstanceOf[T]

  override def max(other: T): T = this.asInstanceOf[T]

  override def floor: T = this.asInstanceOf[T]

  override def min(other: T): T = this.asInstanceOf[T]

  override def ceil: T = this.asInstanceOf[T]

  //====================
  // Read and Write
  //====================

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data <<< yaw
    return data
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("yaw", yaw)
    return nbt
  }
}