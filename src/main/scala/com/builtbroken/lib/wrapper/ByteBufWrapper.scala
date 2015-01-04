package com.builtbroken.lib.wrapper

import com.builtbroken.api.ISave
import com.builtbroken.lib.network.netty.IByteBufObject
import com.builtbroken.lib.transform.vector.{Vector2, Vector3}
import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidTank

/**
 * Some alias methods to make packets more pleasant.
 * @author anti344, Calclavia
 */
object ByteBufWrapper
{

  implicit class ByteBufWrapper(buf: ByteBuf)
  {
    def read[T](sample: T): T =
    {
      return (sample match
      {
        case x: Array[Any] => readArray(x)
        case x: Int => buf.readInt()
        case x: Float => buf.readFloat()
        case x: Double => buf.readDouble()
        case x: Byte => buf.readByte()
        case x: Boolean => buf.readBoolean()
        case x: String => buf.readString()
        case x: Short => buf.readShort()
        case x: Long => buf.readLong()
        case x: IByteBufObject => x.readBytes(buf)
        case x: Vector3 => new Vector3(buf)
        case x: Vector2 => new Vector2(buf)
        case x: NBTTagCompound => buf.readTag()
        case x: FluidTank => buf.readTank()
        case x: ISave => x.load(buf.readTag())
        case _ => throw new IllegalArgumentException("Resonant Engine ByteBuf attempt to read an invalid object [" + sample + "] of class [" + sample.getClass + "]")
      }).asInstanceOf[T]
    }

    def readArray(data: Array[Any]): Array[Any] =
    {
      return data map read
    }

    def readTank() = new FluidTank(buf.readInt()).readFromNBT(readTag())

    def readVector2() = new Vector2(buf)

    def readVector3() = new Vector3(buf)

    def readTag() = ByteBufUtils.readTag(buf)

    def readStack() = ByteBufUtils.readItemStack(buf)

    def readString() = ByteBufUtils.readUTF8String(buf)

    def >>>>(f: (NBTTagCompound => Unit))
    {
      f(buf.readTag())
    }

    def >>>(obj: ISave)
    {
      obj.load(buf.readTag())
    }

    /**
     * Automatically determine how to write a specific piece of data.
     * @param data
     * @return
     */
    def <<<(data: Any): ByteBuf =
    {
      try
      {
        data match
        {
          case x: Array[Any] => this <<< x
          case x: Seq[Any] => this <<< x
          case x: Int => buf <<< x
          case x: Float => buf <<< x
          case x: Double => buf <<< x
          case x: Byte => buf <<< x
          case x: Boolean => buf <<< x
          case x: String => buf <<< x
          case x: Short => buf <<< x
          case x: Long => buf <<< x
          case x: IByteBufObject => x.writeBytes(buf)
          case x: Vector3 => x.writeByteBuf(buf)
          case x: Vector2 => x.writeByteBuf(buf)
          case x: NBTTagCompound => buf <<< x
          case x: FluidTank => buf <<< x
          case x: ISave => buf <<< x
          case _ => throw new IllegalArgumentException("Resonant Engine ByteBuf attempt to write an invalid object [" + data + "] of class [" + data.getClass + "]")
        }
      }
      catch
        {
          case ie: IllegalArgumentException =>
          {
            if (ie.getMessage.contains("Resonant Engine"))
            {
              ie.printStackTrace()
            }
            else
            {
              throw ie
            }
          }
        }
      buf
    }

    def <<<(data: Array[Any]): ByteBuf =
    {
      data foreach (this <<< _)
      buf
    }

    def <<<(data: Seq[Any]): ByteBuf =
    {
      data foreach (this <<< _)
      buf
    }

    def <<<<(f: (NBTTagCompound => Unit))
    {
      val nbt = new NBTTagCompound
      f(nbt)
      buf <<< nbt
    }

    def <<<(tank: FluidTank): ByteBuf =
    {
      buf <<< tank.getCapacity
      buf <<< tank.writeToNBT(new NBTTagCompound)
      buf
    }

    def <<<(saveObj: ISave): ByteBuf =
    {
      val nbt = new NBTTagCompound
      saveObj.save(nbt)
      buf <<< nbt
      buf
    }

    def <<<(boolean: Boolean): ByteBuf =
    {
      buf.writeBoolean(boolean)
      buf
    }

    def <<<(byte: Byte): ByteBuf =
    {
      buf.writeByte(byte)
      buf
    }

    def <<<(short: Short): ByteBuf =
    {
      buf.writeShort(short)
      buf
    }

    def <<<(int: Int): ByteBuf =
    {
      buf.writeInt(int)
      buf
    }

    def <<<(long: Long): ByteBuf =
    {
      buf.writeLong(long)
      buf
    }

    def <<<(float: Float): ByteBuf =
    {
      buf.writeFloat(float)
      buf
    }

    def <<<(double: Double): ByteBuf =
    {
      buf.writeDouble(double)
      buf
    }

    def <<<(nbt: NBTTagCompound): ByteBuf =
    {
      ByteBufUtils.writeTag(buf, nbt)
      buf
    }

    def <<<(stack: ItemStack): ByteBuf =
    {
      ByteBufUtils.writeItemStack(buf, stack)
      buf
    }

    def <<<(str: String): ByteBuf =
    {
      ByteBufUtils.writeUTF8String(buf, str)
      buf
    }
  }

}