package resonant.lib.network.handle;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import resonant.lib.network.discriminator.PacketType;

/**
 * Created by robert on 8/5/2014.
 */
public interface IPacketReceiver
{
    /**
     * Reads a packet
     * @param buf   - data encoded into the packet
     * @param player - player that is receiving the packet
     * @param packet - The packet instance that was sending this packet.
     */
    public boolean read(ByteBuf buf, EntityPlayer player, PacketType packet);

}
