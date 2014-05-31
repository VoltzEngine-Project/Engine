package resonant.lib.network.netty;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import resonant.lib.network.PacketAnnotation;
import resonant.lib.network.PacketEntity;
import resonant.lib.network.PacketPlayerItem;
import resonant.lib.network.PacketTile;

/**
 * @author tgame14
 * @since 31/05/14
 */
public class ResonantChannelHandler extends FMLIndexedMessageToMessageCodec<AbstractPacket>
{
	public ResonantChannelHandler()
	{
		this.addDiscriminator(0, PacketTile.class);
		this.addDiscriminator(1, PacketEntity.class);
		this.addDiscriminator(2, PacketAnnotation.class);
		this.addDiscriminator(3, PacketPlayerItem.class);

	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, AbstractPacket packet, ByteBuf target) throws Exception
	{
		packet.encodeInto(ctx, target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, AbstractPacket packet)
	{
		packet.decodeInto(ctx, source);
	}
}
