package com.builtbroken.mc.core.network.netty;

import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.BBL;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

/**
 * @author tgame14
 * @since 31/05/14
 */
@ChannelHandler.Sharable
public class ResonantPacketHandler extends SimpleChannelInboundHandler<AbstractPacket>
{
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket packet) throws Exception
	{
		INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();

		switch (FMLCommonHandler.instance().getEffectiveSide())
		{
			case CLIENT:
				packet.handleClientSide(BBL.proxy.getClientPlayer());
				break;
			case SERVER:
				packet.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity);
				break;
			default:
				break;
		}

	}

}
