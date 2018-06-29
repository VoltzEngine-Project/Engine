package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.ex.PacketIDException;
import com.builtbroken.mc.core.network.ex.PacketTileReadException;
import com.builtbroken.mc.imp.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketEntity extends PacketType
{
    protected int entityId;
    protected Object[] dataToWrite;
    protected ByteBuf dataToRead;

    public PacketEntity()
    {
        //Needed for forge to construct the packet
    }

    public PacketEntity(Entity entity, Object... args)
    {
        this.entityId = entity.getEntityId();
        this.dataToWrite = args;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
        data_$eq(buffer);
        for (Object data : dataToWrite)
        {
            writeObject(data);
        }
    }

    protected void writeObject(Object data)
    {
        if (data instanceof Collection)
        {
            for (Object o : (Collection) data)
            {
                writeObject(o);
            }
        }
        else
        {
            write(data);
        }
    }


    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.entityId = buffer.readInt();
        data_$eq(dataToRead = buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handleServerSide(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Entity entity = player.getEntityWorld().getEntityByID(this.entityId);

        sender_$eq(player);
        if (entity instanceof IPacketIDReceiver)
        {
            if (((IPacketIDReceiver) entity).shouldReadPacket(player, new Location(entity), this))
            {
                try
                {
                    IPacketIDReceiver receiver = (IPacketIDReceiver) entity;
                    ByteBuf buf = dataToRead;

                    int id;
                    try
                    {
                        id = buf.readInt();
                    }
                    catch (IndexOutOfBoundsException ex)
                    {
                        Engine.logger().error(new PacketIDException(new Location(entity)));
                        return;
                    }
                    receiver.read(buf, id, player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.logger().error(new PacketTileReadException(new Location(entity), "Packet was read past it's size."));
                    Engine.logger().error("Error: ", e);
                }
                catch (NullPointerException e)
                {
                    Engine.logger().error(new PacketTileReadException(new Location(entity), "Null pointer while reading data", e));
                    Engine.logger().error("Error: ", e);
                }
                catch (Exception e)
                {
                    Engine.logger().error(new PacketTileReadException(new Location(entity), "Failed to read packet", e));
                    Engine.logger().error("Error: ", e);
                }
            }
            else
            {
                Engine.logger().error("Error: " + entity + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else if (entity instanceof IPacketReceiver)
        {
            if (((IPacketReceiver) entity).shouldReadPacket(player, new Location(entity), this))
            {
                try
                {
                    IPacketReceiver receiver = (IPacketReceiver) entity;
                    receiver.read(dataToRead, player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.logger().error(new PacketTileReadException(new Location(entity), "Packet was read past it's size."));
                }
                catch (Exception e)
                {
                    Engine.logger().error(new PacketTileReadException(new Location(entity), "Failed to read packet", e));
                    e.printStackTrace();
                }
            }
            else
            {
                Engine.logger().error("Error: " + entity + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else
        {
            Engine.logger().error(new PacketTileReadException(new Location(entity), "Unsupported action for " + entity));
        }
    }
}
