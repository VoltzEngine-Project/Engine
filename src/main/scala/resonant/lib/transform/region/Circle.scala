package resonant.lib.transform.region

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import resonant.lib.transform.vector.IVector2
import resonant.lib.transform.vector.Vector2

/** Simple point with a radius. Can be used for just about anything including
  * Collision boxes, detection areas, effects, and GUIs
  *
  * Yes i put circles in MC
  * ....................../´¯/)
  * ....................,/¯../
  * .................../..../ 
  * ............./´¯/'...'/´¯¯·¸
  * ........../'/.../..../......./¨¯\
  * ........('(...´...´.... ¯~/'...')
  * .........\.................'..../
  * ..........\.....\.......... _.·´
  * ............\..............(
  * ..............\.............\...
 * Created by robert on 12/17/2014.
 */
class Circle(var center: Vector2, var r: Double) extends Shape[Circle]
{
  def this(center: Vector2)
  {
    this(center, 1)
  }

  def getArea: Double =
  {
    return Math.PI * (r * r)
  }

  def isWithin2D(vec: IVector2): Boolean =
  {
    return center.distance(new Vector2(vec.x, vec.y)) <= r
  }

  override def set(other: Circle): Circle =
  {
    this.center = other.center
    this.r = other.r
    return this
  }

  override def +(amount: Double): Circle = new Circle(center, r  + amount)

  override def +(amount: Circle): Circle = new Circle(center.midpoint(amount.center), r + amount.r)

  override def writeByteBuf(data: ByteBuf): ByteBuf = data

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound = nbt

  override def *(amount: Double): Circle = new Circle(center, r  * amount)

  override def *(amount: Circle): Circle = new Circle(center.midpoint(amount.center), r * amount.r)
}