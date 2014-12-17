package resonant.lib.transform.region

import io.netty.buffer.ByteBuf
import resonant.lib.wrapper.ByteBufWrapper._
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import resonant.lib.transform.vector.{Vector2, IVector2}

/** Triangle shape. Assumes that connections go
  * a -> b -> c -> a forming a triangle shape
 * Created by robert on 12/14/2014.
 */
class Triangle(var a: IVector2, var b: IVector2, var c: IVector2) extends Shape[Triangle]
{
  override def getArea: Double = Math.abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) /  2

  override def isWithin2D(vec: IVector2): Boolean = ???

  override def set(other: Triangle): Triangle =
  {
    this.a = other.a;
    this.b = other.b;
    this.c = other.c;
    return this
  }

  override def +(amount: Double): Triangle = new Triangle(new Vector2(a.x + amount, a.y + amount), new Vector2(b.x + amount, b.y + amount), new Vector2(c.x + amount, c.y + amount))

  override def +(t: Triangle): Triangle =
  {
    val newA = new Vector2(a.x + t.a.x, a.y + t.a.y)
    val newB = new Vector2(b.x + t.b.x, b.y + t.b.y)
    val newC  = new Vector2(b.x + t.b.x, b.y + t.b.y)
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
    list.appendTag(new Vector2(a.x, a.y).writeNBT(nbt))
    list.appendTag(new Vector2(b.x, b.y).writeNBT(nbt))
    list.appendTag(new Vector2(c.x, c.y).writeNBT(nbt))
    nbt.setTag("abc", list);
    return nbt
  }

  override def *(amount: Double): Triangle = new Triangle(new Vector2(a.x * amount, a.y * amount), new Vector2(b.x * amount, b.y * amount), new Vector2(c.x * amount, c.y * amount))

  override def *(t: Triangle): Triangle =
  {
    val newA = new Vector2(a.x * t.a.x, a.y * t.a.y)
    val newB = new Vector2(b.x * t.b.x, b.y * t.b.y)
    val newC  = new Vector2(b.x * t.b.x, b.y * t.b.y)
    return new Triangle(newA, newB, newC)
  }
}