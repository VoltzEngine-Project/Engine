package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;

/**
 * @since 25/05/14
 * @author tgame14
 */
public interface INettyPacket
{
    /**
     * Implement this method to write to a ByteBuf data you need to send in the packet
     *
     * @param buffer the {@link io.netty.buffer.ByteBuf} that you write to
     */
    void toBytes(ByteBuf buffer);

    /**
     *
     * @param buffer the {@link io.netty.buffer.ByteBuf} that you read from
     */
    void fromBytes(ByteBuf buffer);
}
