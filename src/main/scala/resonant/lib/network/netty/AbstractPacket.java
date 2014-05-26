package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @since 26/05/14
 * @author tgame14
 */
public abstract class AbstractPacket
{

    /**
     * Encode the packet data into the ByteBuf stream. Complex data sets may need specific data handlers
     * @See {@link cpw.mods.fml.common.network.ByteBufUtils}
     *
     * @param ctx    channel context
     * @param buffer the buffer to encode into
     */
    public abstract void encodeInto (ChannelHandlerContext ctx, ByteBuf buffer);

    /**
     * Decode the packet data from the ByteBuf stream. Complex data sets may need specific data handlers
     * @See {@link cpw.mods.fml.common.network.ByteBufUtils}
     *
     * @param ctx    channel context
     * @param buffer the buffer to decode from
     */
    public abstract void decodeInto (ChannelHandlerContext ctx, ByteBuf buffer);

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
