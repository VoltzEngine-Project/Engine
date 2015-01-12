package com.builtbroken.mc.lib.transform.region

import com.builtbroken.jlib.data.vector.IPos2D
import io.netty.buffer.ByteBuf
import com.builtbroken.mc.lib.helper.wrapper.ByteBufWrapper._
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import com.builtbroken.mc.lib.transform.vector.Point

/** Triangle shape. Assumes that connections go
  * a -> b -> c -> a forming a triangle shape
 * Created by robert on 12/14/2014.
 */
class Triangle(var a: IPos2D, var b: IPos2D, var c: IPos2D) extends Shape[Triangle]
{
  override def getArea: Double = Math.abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) /  2

  override def isWithin2D(vec: IPos2D): Boolean = ???

  override def set(other: Triangle): Triangle =
  {
    this.a = other.a;
    this.b = other.b;
    this.c = other.c;
    return this
  }


  /** Checks if the point is inside the shape */
  override def isWithin(x: Double, y: Double): Boolean =
  {
    val p = new Point(x, y)
    var ab = new Triangle(a, b, p).getArea
    var bc = new Triangle(b, c, p).getArea
    var ca = new Triangle(c, a, p).getArea
    return (ab + bc + ca) <= getArea
  }

  override def +(amount: Double): Triangle = new Triangle(new Point(a.x + amount, a.y + amount), new Point(b.x + amount, b.y + amount), new Point(c.x + amount, c.y + amount))

  override def +(t: Triangle): Triangle =
  {
    val newA = new Point(a.x + t.a.x, a.y + t.a.y)
    val newB = new Point(b.x + t.b.x, b.y + t.b.y)
    val newC  = new Point(b.x + t.b.x, b.y + t.b.y)
    return new Triangle(newA, newB, newC)
  }

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data <<< a
    data <<< b
    data <<< c
    return data
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    val list: NBTTagList  = new NBTTagList();
    list.appendTag(new Point(a.x, a.y).toNBT)
    list.appendTag(new Point(b.x, b.y).toNBT)
    list.appendTag(new Point(c.x, c.y).toNBT)
    nbt.setTag("abc", list);
    return nbt
  }

  override def *(amount: Double): Triangle = new Triangle(new Point(a.x * amount, a.y * amount), new Point(b.x * amount, b.y * amount), new Point(c.x * amount, c.y * amount))

  override def *(t: Triangle): Triangle =
  {
    val newA = new Point(a.x * t.a.x, a.y * t.a.y)
    val newB = new Point(b.x * t.b.x, b.y * t.b.y)
    val newC  = new Point(b.x * t.b.x, b.y * t.b.y)
    return new Triangle(newA, newB, newC)
  }

  /** Distance the shape takes in the X axis */
  override def getSizeX: Double =
  {
    var lower : Double = a.x
    var upper : Double = a.x

    if(b.x < lower)
      lower = b.x
    if(c.x < lower)
      lower = c.x

    if(b.x > upper)
      upper = b.x
    if(c.x > upper)
      upper = c.x

    return upper - lower
  }

  /** Distance the shape takes in the Y axis */
  override def getSizeY: Double =
  {
    var lower : Double = a.y
    var upper : Double = a.y

    if(b.y < lower)
      lower = b.y
    if(c.y < lower)
      lower = c.y

    if(b.y > upper)
      upper = b.y
    if(c.y > upper)
      upper = c.y

    return upper - lower
  }

}