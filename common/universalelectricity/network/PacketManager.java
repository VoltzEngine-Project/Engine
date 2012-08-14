package universalelectricity.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.basiccomponents.BasicComponents;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * This class is used for sending and receiving packets between the server
 * and the client. You can directly use this by registering this packet manager
 * with NetworkMod. Example:
 * @NetworkMod(channels = { "BasicComponents" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
 * 
 * Check out {@link #BasicComponents} for better reference.
 * 
 * @author Calclavia
 */
public class PacketManager implements IPacketHandler
{
	@Override
	public void onPacketData(NetworkManager network, Packet250CustomPayload packet, Player player)
	{
		try
        {
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			
			World world = BasicComponents.proxy.getWorld();
			
			if(world != null)
			{
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				
				if(tileEntity != null)
				{
					if(tileEntity instanceof IPacketReceiver)
					{
						((IPacketReceiver)tileEntity).handlePacketData(network, packet.channel, data);
						
					}
				}
			}
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
	
	
	public static void sendTileEntityPacket(TileEntity sender, String channelName, double[] sendData)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(sender.xCoord);
            data.writeInt(sender.yCoord);
            data.writeInt(sender.zCoord);

            for(double dataValue : sendData)
            {
                data.writeDouble(dataValue);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channelName;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        ModLoader.getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
    }
}
