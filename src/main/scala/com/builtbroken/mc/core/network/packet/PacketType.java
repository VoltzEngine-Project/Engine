package com.builtbroken.mc.core.network.packet;

import com.builtbroken.jlib.data.network.IByteBufWriter;
import com.builtbroken.jlib.data.vector.IPos2D;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.data.IPacket;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/29/2018.
 */
public abstract class PacketType implements IPacket
{
    EntityPlayer sender = null;

    private final List<Object> dataToWrite = new ArrayList();
    private ByteBuf dataToRead;

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dataToWrite.forEach(d -> write(d, buffer));
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dataToRead = buffer;
    }

    @Override
    public <A extends IPacket> A add(Object data)
    {
        dataToWrite.add(data);
        return (A) this;
    }

    protected ByteBuf getDataToRead()
    {
        return dataToRead;
    }

    protected void write(Object object, ByteBuf data)
    {
        if (object instanceof Consumer)
        {
            ((Consumer) object).accept(data);
        }
        else if (object instanceof Collection)
        {
            ((Collection) object).forEach(e -> write(e, data));
        }
        else if (object.getClass().isArray())
        {
            Object[] arrays = (Object[]) object;
            for (Object o : arrays)
            {
                write(o, data);
            }
        }
        else if (object instanceof Integer)
        {
            data.writeInt((Integer) object);
        }
        else if (object instanceof Float)
        {
            data.writeFloat((Float) object);
        }
        else if (object instanceof Double)
        {
            data.writeDouble((Double) object);
        }
        else if (object instanceof Short)
        {
            data.writeShort((Short) object);
        }
        else if (object instanceof Long)
        {
            data.writeLong((Long) object);
        }
        else if (object instanceof Byte)
        {
            data.writeByte((Byte) object);
        }
        else if (object instanceof Boolean)
        {
            data.writeBoolean((Boolean) object);
        }
        else if (object instanceof String)
        {
            ByteBufUtils.writeUTF8String(data, (String) object);
        }
        else if (object instanceof IByteBufWriter)
        {
            ((IByteBufWriter) object).writeBytes(data);
        }
        else if (object instanceof IPos3D)
        {
            data.writeDouble(((IPos3D) object).x());
            data.writeDouble(((IPos3D) object).y());
            data.writeDouble(((IPos3D) object).z());
        }
        else if (object instanceof IPos2D)
        {
            data.writeDouble(((IPos2D) object).x());
            data.writeDouble(((IPos2D) object).y());
        }
        else if (object instanceof NBTTagCompound)
        {
            ByteBufUtils.writeTag(data, (NBTTagCompound) object);
        }
        else if (object instanceof FluidTank)
        {
            ByteBufUtils.writeTag(data, ((FluidTank) object).writeToNBT(new NBTTagCompound()));
        }
        else if (object instanceof FluidStack)
        {
            ByteBufUtils.writeTag(data, ((FluidStack) object).writeToNBT(new NBTTagCompound()));
        }
        else if (object instanceof ItemStack)
        {
            ByteBufUtils.writeItemStack(data, (ItemStack) object);
        }
        else if (object instanceof ISave)
        {
            ByteBufUtils.writeTag(data, ((ISave) object).save(new NBTTagCompound()));
        }
        else if (object instanceof Enum)
        {
            data.writeInt(((Enum) object).ordinal());
        }
    }
}
