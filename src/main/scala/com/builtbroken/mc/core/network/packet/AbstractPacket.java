package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.network.IByteBufWriter;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For custom packets extend this Class and register on Mod loading phase
 * <p/>
 * Without registering a NPE will be thrown as the {@link com.builtbroken.mc.core.network.netty.PacketManager} won't know how to handle it
 * <p/>
 * To send this packet also look at {@link com.builtbroken.mc.core.network.netty.PacketManager#sendToAll(AbstractPacket)}
 * And other implementations there.
 *
 * @author tgame14
 * @since 26/05/14
 */
public abstract class AbstractPacket
{
    public ByteBuf data;
    public int id = 0;

    protected ArrayList<Object> load = new ArrayList();

    public AbstractPacket()
    {
    }

    public AbstractPacket(int id, Object... args)
    {
        this.id = id;
        if (args != null)
        {
            for (Object obj : args)
            {
                load.add(obj);
            }
        }
    }

    public static void write(Object object, ByteBuf data)
    {
        if (object == null)
        {
            if (Engine.runningAsDev)
            {
                References.LOGGER.error("Attempted to write a null object to packet");
                Exception e = new IllegalArgumentException();
                e.printStackTrace();
            }
        }
        else if (object instanceof Object[])
        {
            for (Object obj : (Object[]) object)
            {
                write(obj, data);
            }
        }
        else if (object instanceof Collection)
        {
            for (Object obj : (Collection) object)
            {
                write(obj, data);
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
        else if (object instanceof Short)
        {
            data.writeShort((Short) object);
        }
        else if (object instanceof Long)
        {
            data.writeLong((Long) object);
        }
        else if (object instanceof IByteBufWriter)
        {
            ((IByteBufWriter) object).writeBytes(data);
        }
        else if (object instanceof IPosition)
        {
            data.writeInt((Integer) object);
        }
        else if (object instanceof NBTTagCompound)
        {
            ByteBufUtils.writeTag(data, (NBTTagCompound) object);
        }
        else if (object instanceof ISave)
        {
            ByteBufUtils.writeTag(data, ((ISave) object).save(new NBTTagCompound()));
        }
        else if (Engine.runningAsDev)
        {
            References.LOGGER.error("Attempt to write an invalid object [" + data + "] of class [" + data.getClass() + "]");
        }
    }


    /**
     * Encode the packet data into the ByteBuf stream. Complex data sets may need specific data handlers
     *
     * @param ctx    channel context
     * @param buffer the buffer to encode into
     * @see {@link cpw.mods.fml.common.network.ByteBufUtils}
     * @see {@link com.builtbroken.mc.core.network.netty.PacketManager#writeData(io.netty.buffer.ByteBuf, Object...)}
     */
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(id);
        for (Object obj : load)
        {
            write(obj, buffer);
        }
    }

    /**
     * Decode the packet data from the ByteBuf stream. Complex data sets may need specific data handlers
     *
     * @param ctx    channel context
     * @param buffer the buffer to decode from
     * @See {@link cpw.mods.fml.common.network.ByteBufUtils}
     */
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        data = buffer.slice();
    }

    /**
     * Handle a packet on the client side. Note this occurs after decoding has completed.
     *
     * @param player the player reference
     */
    public void handleClientSide(EntityPlayer player)
    {
    }

    /**
     * Handle a packet on the server side. Note this occurs after decoding has completed.
     *
     * @param player the player reference
     */
    public void handleServerSide(EntityPlayer player)
    {
    }

    public void send()
    {
        Engine.instance.packetHandler.sendToAll(this);
    }
}
