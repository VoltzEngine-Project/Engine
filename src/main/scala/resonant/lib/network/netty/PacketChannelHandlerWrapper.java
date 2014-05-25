package resonant.lib.network.netty;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import org.apache.logging.log4j.Level;
import resonant.core.ResonantEngine;
import resonant.lib.References;

/**
 * @since 25/05/14
 * @author tgame14
 */
public class PacketChannelHandlerWrapper<REQ extends INettyPacket, REPLY extends INettyPacket> extends SimpleChannelInboundHandler<REQ>
{
    private IPacketHander<REQ, REPLY> packetHandler;
    private Side side;

    public PacketChannelHandlerWrapper (Class<? extends IPacketHander<REQ, REPLY>> handler, Side side, Class<REQ> requestType)
    {
        super(requestType);
        try
        {
            packetHandler = handler.newInstance();
        }
        catch (Exception ex)
        {
            Throwables.propagate(ex);
        }
        this.side = side;
    }

    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, REQ msg) throws Exception
    {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        PacketContext context = new PacketContext(netHandler, side);
        REPLY result = packetHandler.onPacket(msg, context);

        if (result != null)
        {
            ctx.writeAndFlush(result).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        References.LOGGER.log(Level.ERROR, "PacketChannelHandlerWrapper Exception", cause);
        super.exceptionCaught(ctx, cause);
    }
}
