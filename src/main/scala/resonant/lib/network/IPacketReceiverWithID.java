package resonant.lib.network;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

/** Same as IPacketReciever by contains an id variable by default to improve sorting of incoming
 * packets without reading the data stream
 * 
 * @author Darkguardsman */
public interface IPacketReceiverWithID
{
    /** @param data - data encoded into the packet
     * @param player - player that sent or is receiving the packet */
    public boolean onReceivePacket(int id, ByteArrayDataInput data, EntityPlayer player, Object... extra);
}
