package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.ex.PacketIDException;
import com.builtbroken.mc.core.network.ex.PacketTileReadException;
import com.builtbroken.mc.lib.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Packet type designed to be used with Tiles
 *
 * @author tgame14
 * @since 26/05/14
 */
public class PacketTile extends PacketType
{
    public int x;
    public int y;
    public int z;

    public PacketTile()
    {

    }

    /**
     * @param x    - location
     * @param y    - location
     * @param z    - location
     * @param args -  data to send, first arg should be packetID if
     *             the tile is an instance of {@code IPacketIDReceiver}
     *             Should never be null
     */
    public PacketTile(int x, int y, int z, Object... args)
    {
        super(args);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @param tile - TileEntity to send this packet to, only used for location data
     * @param args -  data to send, first arg should be packetID if
     *             the tile is an instance of {@code IPacketIDReceiver}
     *             Should never be null
     */
    public PacketTile(TileEntity tile, Object... args)
    {
        this(tile.xCoord, tile.yCoord, tile.zCoord, args);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBytes(data());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        data_$eq(buffer.slice());
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handle(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        handle(player);
    }

    /**
     * Called to handle a packet when it is received
     *
     * @param player - player who received the packet
     */
    public void handle(EntityPlayer player)
    {
        handle(player, player.getEntityWorld().getTileEntity(this.x, this.y, this.z));
    }

    /**
     * Called to handler a packet when it is received
     *
     * @param player - player who received the packet
     * @param tile   - tile who is receiving the packet
     */
    public void handle(EntityPlayer player, TileEntity tile)
    {
        Location location = new Location(player.worldObj, x, y, z);
        sender_$eq(player);
        if (tile == null)
        {
            Engine.instance.logger().error(new PacketTileReadException(location, "Null tile"));
        }
        else if (tile.isInvalid())
        {
            Engine.instance.logger().error(new PacketTileReadException(location, "Invalidated tile"));
        }
        else if (tile instanceof IPacketIDReceiver)
        {
            try
            {
                IPacketIDReceiver receiver = (IPacketIDReceiver) tile;
                ByteBuf buf = data().slice();

                int id;
                try
                {
                    id = buf.readInt();
                } catch (IndexOutOfBoundsException ex)
                {
                    Engine.instance.logger().error(new PacketIDException(location));
                    return;
                }
                receiver.read(buf, id, player, this);
            } catch (IndexOutOfBoundsException e)
            {
                Engine.instance.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
            } catch (Exception e)
            {
                Engine.instance.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
            }
        }
        else if (tile instanceof IPacketReceiver)
        {
            try
            {
                IPacketReceiver receiver = (IPacketReceiver) tile;
                receiver.read(data().slice(), player, this);
            } catch (IndexOutOfBoundsException e)
            {
                Engine.instance.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
            } catch (Exception e)
            {
                Engine.instance.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                e.printStackTrace();
            }
        }
        else
        {
            Engine.instance.logger().error(new PacketTileReadException(location, "Unsupported action for tile"));
        }
    }
}
