package universalelectricity.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.server.ModLoader;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet250CustomPayload;
import net.minecraft.server.TileEntity;
import forge.IPacketHandler;
import forge.MessageManager;


/**
 * The Class PacketManager.
 */
public class PacketManager implements IPacketHandler
{
    
    /** The id. */
    public int id;
    
    /** The channel name. */
    public String channelName;

    /**
     * Instantiates a new packet manager.
     *
     * @param s the s
     */
    public PacketManager(String s)
    {
        channelName = s;
    }

    /**
     * Register channel.
     *
     * @param networkmanager the networkmanager
     */
    public void registerChannel(NetworkManager networkmanager)
    {
        MessageManager.getInstance().registerChannel(networkmanager, this, channelName);
    }

    /* (non-Javadoc)
     * @see forge.IPacketHandler#onPacketData(net.minecraft.server.NetworkManager, java.lang.String, byte[])
     */
    public void onPacketData(NetworkManager networkmanager, String s, byte abyte0[])
    {
    }

    /**
     * Send packet data.
     *
     * @param ipacketsender the ipacketsender
     * @param ad the ad
     */
    public void sendPacketData(IPacketSender ipacketsender, double ad[])
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        try
        {
            dataoutputstream.writeInt(ipacketsender.getPacketID());

            if (ipacketsender instanceof TileEntity)
            {
                dataoutputstream.writeInt(((TileEntity)ipacketsender).x);
                dataoutputstream.writeInt(((TileEntity)ipacketsender).y);
                dataoutputstream.writeInt(((TileEntity)ipacketsender).z);
            }
            else
            {
                dataoutputstream.writeInt(0);
                dataoutputstream.writeInt(0);
                dataoutputstream.writeInt(0);
            }

            double ad1[] = ad;
            int i = ad1.length;

            for (int j = 0; j < i; j++)
            {
                double d = ad1[j];
                dataoutputstream.writeDouble(d);
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }

        Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
        packet250custompayload.tag = channelName;
        packet250custompayload.data = bytearrayoutputstream.toByteArray();
        packet250custompayload.length = packet250custompayload.data.length;
        ModLoader.getMinecraftServerInstance().serverConfigurationManager.sendAll(packet250custompayload);
    }
}
