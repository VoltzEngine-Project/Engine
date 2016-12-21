package com.builtbroken.mc.core.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Base object for all custom packets using in VoltzEngine.
 * </p>
 * Ensure that there is always a default constructor so forge can create the packet.
 * <p/>
 * <p/>
 * An NPE will be thrown if the packet is not registered due to {@link com.builtbroken.mc.core.network.netty.PacketManager} not knowing how to handle it
 * <p/>
 * See {@link com.builtbroken.mc.core.network.netty.PacketManager#sendToAll(AbstractPacket)} for exact usage on sending the packet
 *
 * @author tgame14, DarkCow
 * @since 26/05/14
 */
public abstract class AbstractPacket
{

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

    }

    /**
     * Handle a packet on the client side. Note this occurs after decoding has completed.
     *
     * @param player the player reference
     */
    public void handleClientSide(EntityPlayer player)
    {
        throw new UnsupportedOperationException("Unsupported operation for Packet: " + getClass().getSimpleName());
    }

    /**
     * Handle a packet on the server side. Note this occurs after decoding has completed.
     *
     * @param player the player reference
     */
    public void handleServerSide(EntityPlayer player)
    {
        throw new UnsupportedOperationException("Unsupported operation for Packet: " + getClass().getSimpleName());
    }
}
