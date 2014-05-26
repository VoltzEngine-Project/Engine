package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;

/**
 * @since 26/05/14
 * @author tgame14
 */
public interface INettyPacket
{
    public void readPacket(ByteBuf buf);

    public void loadPacket(ByteBuf buf);
}
