package universalelectricity.network;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

public interface IPacketReceiver
{
    /**
     * Sends the tileEntity the rest of the data
     */
    public void handlePacketData(NetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream);
}
