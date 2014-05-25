package resonant.lib.network.netty;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @since 25/05/14
 * @author tgame14
 */
public class PacketIndexedCodec extends FMLIndexedMessageToMessageCodec<INettyPacket>
{

    @Override
    public void encodeInto (ChannelHandlerContext ctx, INettyPacket msg, ByteBuf target) throws Exception
    {
        msg.toBytes(target);
    }

    @Override
    public void decodeInto (ChannelHandlerContext ctx, ByteBuf source, INettyPacket msg)
    {
        msg.fromBytes(source);
    }
}
