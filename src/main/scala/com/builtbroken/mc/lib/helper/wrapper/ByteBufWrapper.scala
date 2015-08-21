package com.builtbroken.mc.lib.helper.wrapper

import com.builtbroken.mc.api.ISave
import com.builtbroken.mc.core.{Engine, References}
import com.builtbroken.mc.core.network.{IByteBufWriter, IByteBufReader}
import com.builtbroken.mc.lib.transform.vector.{Pos, Point}
import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.{FluidStack, FluidTank}

/**
 * Some alias methods to make packets more pleasant.
 * @author anti344, Calclavia
 */
object ByteBufWrapper {

  implicit class ByteBufWrapper(buf: ByteBuf) {
    def read[T](sample: T): T = {
      return (sample match {
        case x: Array[Any] => readArray(x)
        case x: Int => buf.readInt()
        case x: Float => buf.readFloat()
        case x: Double => buf.readDouble()
        case x: Byte => buf.readByte()
        case x: Boolean => buf.readBoolean()
        case x: String => buf.readString()
        case x: Short => buf.readShort()
        case x: Long => buf.readLong()
        case x: IByteBufReader => x.readBytes(buf)
        case x: Pos => new Pos(buf)
        case x: Point => new Point(buf)
        case x: NBTTagCompound => buf.readTag()
        case x: FluidTank => buf.readTank()
        case x: ISave => x.load(buf.readTag())
        case _ => throw new IllegalArgumentException("Resonant Engine ByteBuf attempt to read an invalid object [" + sample + "] of class [" + sample.getClass + "]")
      }).asInstanceOf[T]
    }

    def readArray(data: Array[Any]): Array[Any] = {
      return data map read
    }

    def readTank() = new FluidTank(buf.readInt()).readFromNBT(readTag())

    def readVector2() = new Point(buf)

    def readVector3() = new Pos(buf)

    def readTag() = ByteBufUtils.readTag(buf)

    def readStack() = ByteBufUtils.readItemStack(buf)

    def readString() = ByteBufUtils.readUTF8String(buf)

    def >>>>(f: (NBTTagCompound => Unit)) {
      f(buf.readTag())
    }

    def >>>(obj: ISave) {
      obj.load(buf.readTag())
    }

    /**
     * Automatically determine how to write a specific piece of data.
     * @param data
     * @return
     */
    def <<<(data: Any): ByteBuf = {
      if (data == null) {
        if (Engine.runningAsDev)
          Engine.instance.logger().error("Attempted to write null to ByteBuf ", new RuntimeException())
        return buf
      }
      try {
        data match {
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
          case x: IByteBufWriter => x.writeBytes(buf)
          case x: Pos => x.writeByteBuf(buf)
          case x: NBTTagCompound => buf <<< x
          case x: FluidTank => buf <<< x
          case x: ISave => buf <<< x
          case x: ItemStack => ByteBufUtils.writeItemStack(buf, x)
          case _ => throw new IllegalArgumentException(References.NAME + ": ByteBuf attempt to write an invalid object [" + data + "] of class [" + data.getClass + "]")
        }
      }
      catch {
        case ie: IllegalArgumentException =>

          if (ie.getMessage.contains(References.NAME)) {
            ie.printStackTrace()
          }
          else {
            throw ie
          }
      }
      buf
    }

    def <<<(data: Array[Any]): ByteBuf = {
      data foreach (this <<< _)
      buf
    }

    def <<<(data: Seq[Any]): ByteBuf = {
      data foreach (this <<< _)
      buf
    }

    def <<<<(f: (NBTTagCompound => Unit)) {
      val nbt = new NBTTagCompound
      f(nbt)
      buf <<< nbt
    }

    def <<<(tank: FluidTank): ByteBuf = {
      buf <<< tank.getCapacity
      buf <<< tank.writeToNBT(new NBTTagCompound)
      buf
    }

    def <<<(stack: FluidStack): ByteBuf = {
      buf <<< stack.writeToNBT(new NBTTagCompound)
      buf
    }

    def <<<(saveObj: ISave): ByteBuf = {
      val nbt = new NBTTagCompound
      saveObj.save(nbt)
      buf <<< nbt
      buf
    }

    def <<<(boolean: Boolean): ByteBuf = {
      buf.writeBoolean(boolean)
      buf
    }

    def <<<(byte: Byte): ByteBuf = {
      buf.writeByte(byte)
      buf
    }

    def <<<(short: Short): ByteBuf = {
      buf.writeShort(short)
      buf
    }

    def <<<(int: Int): ByteBuf = {
      buf.writeInt(int)
      buf
    }

    def <<<(long: Long): ByteBuf = {
      buf.writeLong(long)
      buf
    }

    def <<<(float: Float): ByteBuf = {
      buf.writeFloat(float)
      buf
    }

    def <<<(double: Double): ByteBuf = {
      buf.writeDouble(double)
      buf
    }

    def <<<(nbt: NBTTagCompound): ByteBuf = {
      ByteBufUtils.writeTag(buf, nbt)
      buf
    }

    def <<<(stack: ItemStack): ByteBuf = {
      ByteBufUtils.writeItemStack(buf, stack)
      buf
    }

    def <<<(str: String): ByteBuf = {
      ByteBufUtils.writeUTF8String(buf, str)
      buf
    }
  }

}