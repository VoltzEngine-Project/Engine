package resonant.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import resonant.core.ResonantEngine;
import resonant.lib.network.netty.AbstractPacket;

/**
 * @since 26/05/14
 * @author tgame14
 */
public abstract class PacketType extends AbstractPacket
{
    protected ByteBuf data;

    public PacketType (Object... args)
    {
        this.data = Unpooled.buffer();
        ResonantEngine.INSTANCE.packetHandler.writeData(this.data, args);
    }
}
