package com.builtbroken.mc.lib.transform.region

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import com.builtbroken.mc.lib.transform.rotation.IRotation
import com.builtbroken.mc.lib.transform.vector.{Vector3, IVector2, IVector3, TVector3}
import com.builtbroken.mc.lib.helper.wrapper.ByteBufWrapper._

/** Prefab for any 3D shape, any 2D shape that is used with this prefab will switch y for z as it
  * assumes the 2D shape horizontal with the world. This also means the y will be replaced with the
  * 3D shape's y. Meaning the 2D shape will be on the center origin of the 3D shape.
  *
 * Created by robert on 12/18/2014.
 */
abstract class Shape3D[T <: Shape3D[T]](var center: IVector3) extends Shape[T] with TVector3 with IRotation
{
  var pitch: Double = 0;
  var roll: Double = 0;

  def this(nbt: NBTTagCompound) =
  {
    this(new Vector3(nbt.getCompoundTag("center")))
    yaw = nbt.getDouble("yaw")
    pitch = nbt.getDouble("pitch")
    roll = nbt.getDouble("roll")
  }

  def this(data: ByteBuf) =
  {
    this(new Vector3(data))
    yaw = data.readDouble
    pitch = data.readDouble
    roll = data.readDouble
  }

  override def set(other: T): T =
  {
    this.center = other.center
    this.yaw = other.yaw
    this.pitch = other.pitch
    this.roll = other.roll
    return this.asInstanceOf[T]
  }

  /** Distance the shape takes in the Z axis */
  def getSizeZ: Double

  /** gets the max distance a corner of the shape will reach from the center  */
  def getSize: Double =
  {
    var r = getSizeX;
    if(getSizeY > r)
      r = getSizeY
    if(getSizeZ > r)
      r = getSizeZ
    return r
  }

  /** Volume of the 3D shape */
  def getVolume: Double

  def isColliding(shape: Shape[_]): Boolean =
  {
    return false
  }

  /** Is the vector(x, y, z) inside the shape */
  def isWithin(x: Double, y: Double, z: Double): Boolean

  /** Center of the 3D shape */
  def getCenter: IVector3 = center

  /** Is the vector(x, z) inside the shape */
  override def isWithin(x: Double, z: Double): Boolean = isWithin(x, this.y, z)

  /** Is the vector(x, z) inside the shape */
  override def isWithin2D(vec: IVector2): Boolean = isWithin(vec.x, this.y, vec.y)

  /** Is the vector(x, y, z) inside the shape */
  override def isWithin(vec: IVector3): Boolean = isWithin(vec.x, vec.y, vec.z)

  override def x: Double = getCenter.x()

  override def y: Double = getCenter.y()

  override def z: Double = getCenter.z()

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data <<< center
    super.writeByteBuf(data)
    data <<< pitch
    data <<< roll
    return data
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setTag("center", new Vector3(center).writeNBT(new NBTTagCompound))
    super.writeNBT(nbt)
    nbt.setDouble("pitch", pitch)
    nbt.setDouble("roll", roll)
    return nbt
  }
}
