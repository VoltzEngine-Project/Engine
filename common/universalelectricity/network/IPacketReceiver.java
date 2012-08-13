package universalelectricity.network;

import java.io.DataInputStream;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.src.NetworkManager;

public interface IPacketReceiver
{
    /**
     * Sends the tileEntity the rest of the data
     */
    public void handlePacketData(NetworkManager network, String channel, ByteArrayDataInput dataStream);
}
