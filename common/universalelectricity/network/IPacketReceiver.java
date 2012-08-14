package universalelectricity.network;

import net.minecraft.src.NetworkManager;

import com.google.common.io.ByteArrayDataInput;

public interface IPacketReceiver
{
    /**
     * Sends the tileEntity the rest of the data
     */
    public void handlePacketData(NetworkManager network, String channel, ByteArrayDataInput dataStream);
}
