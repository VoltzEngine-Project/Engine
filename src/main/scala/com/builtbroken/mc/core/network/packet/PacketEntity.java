package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.ex.PacketIDException;
import com.builtbroken.mc.core.network.ex.PacketTileReadException;
import com.builtbroken.mc.imp.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketEntity extends PacketType
{
    public PacketEntity()
    {
        //Needed for forge to construct the packet
    }

    public PacketEntity(Entity entity)
    {
        add(entity.getEntityId());
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handleServerSide(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        sender = player;

        final ByteBuf data = getDataToRead();
        final int entityID = data.readInt();

        final Entity entity = player.getEntityWorld().getEntityByID(entityID);
        if (entity instanceof IPacketIDReceiver)
        {
            if (((IPacketIDReceiver) entity).shouldReadPacket(player, new Location(entity), this))
            {
                try
                {
                    IPacketIDReceiver receiver = (IPacketIDReceiver) entity;

                    int id;
                    try
                    {
                        id = data.readInt();
                    }
                    catch (IndexOutOfBoundsException ex)
                    {
                        Engine.logger().error(new PacketIDException(new Location(entity)));
                        return;
                    }
                    receiver.read(data, id, player, this);
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
                    receiver.read(data, player, this);
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
