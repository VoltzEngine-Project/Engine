package resonant.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import resonant.engine.ResonantEngine;
import resonant.lib.network.netty.AbstractPacket;
import resonant.lib.network.netty.PacketManager;

/**
 * @author tgame14
 * @since 26/05/14
 */
public abstract class PacketType extends AbstractPacket
{
	protected ByteBuf data;

	public PacketType(Object... args)
	{
		this.data = Unpooled.buffer();
		PacketManager.writeData(this.data, args);
	}
}
