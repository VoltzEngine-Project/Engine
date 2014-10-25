package resonant.content.factory.resources;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import resonant.lib.network.discriminator.PacketType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robert on 8/22/2014.
 */
public class ResourceFactoryPacket extends PacketType
{

	ResourceFactoryHandler factory;

	public ResourceFactoryPacket(ResourceFactoryHandler factory)
	{
		this.factory = factory;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(factory.materialIds.size());
		for (Map.Entry<Integer, String> entry : factory.materialIds.entrySet())
		{
			buffer.writeInt(entry.getKey());
			ByteBufUtils.writeUTF8String(buffer, entry.getValue());
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		HashMap<Integer, String> map = new HashMap();
		for (int i = 0; i < buffer.readInt(); i++)
		{
			map.put(buffer.readInt(), ByteBufUtils.readUTF8String(buffer));
		}
		factory.materialIds.clear();
		factory.materialIds.putAll(map);
	}
}
