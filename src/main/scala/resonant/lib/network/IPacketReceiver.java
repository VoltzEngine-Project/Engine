package resonant.lib.network;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

/** Implement this if an object can receive a packet.
 * 
 * @author Calclavia */
public interface IPacketReceiver
{
    /** @param data - data encoded into the packet
     * @param player - player that sent or is receiving the packet */
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra);
}
