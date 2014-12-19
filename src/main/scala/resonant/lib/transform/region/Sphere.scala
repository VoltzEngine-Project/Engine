package resonant.lib.transform.region

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import resonant.lib.transform.vector.{IVector3, Vector3}
import resonant.lib.wrapper.ByteBufWrapper._

/** 3D Ball shaped region in the world.
  * Can be used for collision boxes, and entity detection
  *
 * Created by robert on 12/18/2014.
 */
class Sphere(c: IVector3, var r: Double) extends Shape3D[Sphere](c)
{
  override def set(other: Sphere): Sphere =
  {
    super.set(other)
    this.r = other.r
    return this
  }

  override def +(amount: Double): Sphere = new Sphere(center, r + amount)

  override def +(amount: Sphere): Sphere = new Sphere(new Vector3(center).midPoint(amount.center), r + amount.r)

  override def *(amount: Double): Sphere = new Sphere(center, r * amount)

  override def *(amount: Sphere): Sphere = new Sphere(new Vector3(center).midPoint(amount.center), r * amount.r)

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    super.writeByteBuf(data)
    data <<< r
    return data
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    super.writeNBT(nbt)
    nbt.setDouble("radius", r)
    return nbt
  }

  override def getSizeX: Double = r * 2

  override def getSizeY: Double = r * 2

  override def getSizeZ: Double = r * 2

  override def getArea: Double = 4 * Math.PI * (r * r)

  override def getVolume: Double = (4 * Math.PI * (r * r * r)) / 3

  override def isWithin(x: Double, y: Double, z: Double): Boolean = new Vector3(x, y, z).subtract(x, y, z).magnitude <= this.r

}
