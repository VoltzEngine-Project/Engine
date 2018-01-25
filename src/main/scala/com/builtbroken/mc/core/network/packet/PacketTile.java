package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.ex.PacketIDException;
import com.builtbroken.mc.core.network.ex.PacketTileReadException;
import com.builtbroken.mc.imp.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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

    public String name;

    public PacketTile()
    {
        //Needed for forge to construct the packet
    }

    /**
     * @param x    - location
     * @param y    - location
     * @param z    - location
     * @param args -  data to send, first arg should be packetID if
     *             the tile is an instance of {@code IPacketIDReceiver}
     *             Should never be null
     */
    public PacketTile(String name, int x, int y, int z, Object... args)
    {
        super(args);
        this.name = name;
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
    public PacketTile(String name, TileEntity tile, Object... args)
    {
        this(name, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), args);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeBoolean(Engine.runningAsDev);
        if(Engine.runningAsDev)
        {
            ByteBufUtils.writeUTF8String(buffer, name);
        }
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBytes(data());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        if(buffer.readBoolean())
        {
            name = ByteBufUtils.readUTF8String(buffer);
        }
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        data_$eq(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (player != null)
        {
            handle(player);
        }
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("PacketTile#handleClientSide(null) - player was null for packet", new RuntimeException());
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        if (player != null)
        {
            handle(player);
        }
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("PacketTile#handleServerSide(null) - player was null for packet", new RuntimeException());
        }
    }

    /**
     * Called to handle a packet when it is received
     *
     * @param player - player who received the packet
     */
    public void handle(EntityPlayer player)
    {
        if (player.getEntityWorld() == null)
        {
            if (Engine.runningAsDev)
            {
                Engine.logger().error("PacketTile#handle(" + player + ") - world is null for player while handling packet. ", new RuntimeException());
            }
            return;
        }
        handle(player, player.getEntityWorld().getTileEntity(new BlockPos(this.x, this.y, this.z)));
    }

    /**
     * Called to handler a packet when it is received
     *
     * @param player - player who received the packet
     * @param tile   - tile who is receiving the packet
     */
    public void handle(EntityPlayer player, TileEntity tile)
    {
        //TODO add checksum or hash to verify the packet is sent to the correct tile
        final Location location = new Location(player.world, x, y, z);
        sender_$eq(player);
        if (tile == null)
        {
            Engine.logger().error(new PacketTileReadException(location, "Null tile"));
        }
        else if (tile.isInvalid())
        {
            Engine.logger().error(new PacketTileReadException(location, "Invalidated tile"));
        }
        else if (tile instanceof IPacketIDReceiver)
        {
            if (((IPacketIDReceiver) tile).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketIDReceiver receiver = (IPacketIDReceiver) tile;
                    ByteBuf buf = data();

                    int id;
                    try
                    {
                        id = buf.readInt();
                    }
                    catch (IndexOutOfBoundsException ex)
                    {
                        Engine.logger().error(new PacketIDException(location, name));
                        return;
                    }
                    receiver.read(buf, id, player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
                    Engine.logger().error("Error: ", e);
                }
                catch (NullPointerException e)
                {
                    Engine.logger().error(new PacketTileReadException(location, "Null pointer while reading data", e));
                    Engine.logger().error("Error: ", e);
                }
                catch (Exception e)
                {
                    Engine.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                    Engine.logger().error("Error: ", e);
                }
            }
            else
            {
                Engine.logger().error("Error: " + tile + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else if (tile instanceof IPacketReceiver)
        {
            if (((IPacketReceiver) tile).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketReceiver receiver = (IPacketReceiver) tile;
                    receiver.read(data().slice(), player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
                }
                catch (Exception e)
                {
                    Engine.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                    e.printStackTrace();
                }
            }
            else
            {
                Engine.logger().error("Error: " + tile + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else
        {
            Engine.logger().error(new PacketTileReadException(location, "Unsupported action for " + tile));
        }
    }
}
