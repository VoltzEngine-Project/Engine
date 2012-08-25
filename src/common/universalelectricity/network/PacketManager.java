package universalelectricity.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
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
public class PacketManager implements IPacketHandler, IPacketReceiver
{
	public enum PacketType
	{
		UNSPECIFIED, TILEENTITY;
		
	    public static PacketType get(int id)
		{
	    	if (id >= 0 && id < PacketType.values().length)
	        {
	            return PacketType.values()[id];
	        }
	        return UNSPECIFIED;
		}
	}
	
	@Override
	public void onPacketData(NetworkManager network, Packet250CustomPayload packet, Player player)
	{
		try
        {
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			
			PacketType packetType = PacketType.get(data.readInt());
			
			if(packetType == PacketType.TILEENTITY)
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				
				World world = ((EntityPlayer)player).worldObj;
				
				if(world != null)
				{
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					
					if(tileEntity != null)
					{
						if(tileEntity instanceof IPacketReceiver)
						{
							((IPacketReceiver)tileEntity).handlePacketData(network, packet, ((EntityPlayer)player), data);
							
						}
					}
				}
			}
			else
			{
				this.handlePacketData(network, packet, ((EntityPlayer)player), data);
			}
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
	
	public static void sendTileEntityPacket(TileEntity sender, String channelName, Object... sendData)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(PacketType.TILEENTITY.ordinal());
        	
            data.writeInt(sender.xCoord);
            data.writeInt(sender.yCoord);
            data.writeInt(sender.zCoord);

            sendPacketToClients(channelName, bytes, data, sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	public static void sendTileEntityPacketToServer(TileEntity sender, String channelName, Object... sendData)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(PacketType.TILEENTITY.ordinal());

            data.writeInt(sender.xCoord);
            data.writeInt(sender.yCoord);
            data.writeInt(sender.zCoord);

            sendPacketToServer(channelName, bytes, data, sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	public static void sendUnspecifiedPacket(String channelName, Object... sendData)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(PacketType.UNSPECIFIED.ordinal());

            sendPacketToClients(channelName, bytes, data, sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	public static void sendUnspecifiedPacketToServer(String channelName, Object... sendData)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(PacketType.UNSPECIFIED.ordinal());

            sendPacketToServer(channelName, bytes, data, sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	public static void sendPacketToClients(String channelName, ByteArrayOutputStream bytes, DataOutputStream data, Object... sendData)
	{
		try
		{
			for(Object dataValue : sendData)
	        {
	        	if(dataValue instanceof Integer)
	        	{
	        		data.writeInt((Integer)dataValue);
	        	}
	        	else if(dataValue instanceof Float)
	        	{
	        		data.writeFloat((Float)dataValue);
	        	}
	        	else if(dataValue instanceof Double)
	        	{
	        		data.writeDouble((Double)dataValue);
	        	}
	        	else if(dataValue instanceof Byte)
	        	{
	        		data.writeByte((Byte)dataValue);
	        	}
	        	else if(dataValue instanceof Boolean)
	        	{
	        		data.writeBoolean((Boolean)dataValue);
	        	}
	        	else if(dataValue instanceof String)
	        	{
	        		data.writeUTF((String)dataValue);
	        	}
	        	else if(dataValue instanceof Short)
	        	{
	        		data.writeShort((Short)dataValue);
	        	}
	        	else if(dataValue instanceof Long)
	        	{
	        		data.writeLong((Long)dataValue);
	        	}
	        }
	        
	        Packet250CustomPayload packet = new Packet250CustomPayload();
	        packet.channel = channelName;
	        packet.data = bytes.toByteArray();
	        packet.length = packet.data.length;
	        
	        if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
	        {
	        	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
	        }
		}
		catch (IOException e)
        {
			System.out.println("Sending packet to client failed.");
            e.printStackTrace();
        }
	}
	
	public static void sendPacketToServer(String channelName, ByteArrayOutputStream bytes, DataOutputStream data, Object... sendData)
	{
		try
		{
			for(Object dataValue : sendData)
	        {
	        	if(dataValue instanceof Integer)
	        	{
	        		data.writeInt((Integer)dataValue);
	        	}
	        	else if(dataValue instanceof Float)
	        	{
	        		data.writeFloat((Float)dataValue);
	        	}
	        	else if(dataValue instanceof Double)
	        	{
	        		data.writeDouble((Double)dataValue);
	        	}
	        	else if(dataValue instanceof Byte)
	        	{
	        		data.writeByte((Byte)dataValue);
	        	}
	        	else if(dataValue instanceof Boolean)
	        	{
	        		data.writeBoolean((Boolean)dataValue);
	        	}
	        	else if(dataValue instanceof String)
	        	{
	        		data.writeUTF((String)dataValue);
	        	}
	        	else if(dataValue instanceof Short)
	        	{
	        		data.writeShort((Short)dataValue);
	        	}
	        	else if(dataValue instanceof Long)
	        	{
	        		data.writeLong((Long)dataValue);
	        	}
	        }
            
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = channelName;
            packet.data = bytes.toByteArray();
            packet.length = packet.data.length;
            PacketDispatcher.sendPacketToServer(packet);
		}
		catch (IOException e)
        {
			System.out.println("Sending packet to server failed.");
            e.printStackTrace();
        }
	}

	@Override
	public void handlePacketData(NetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{

	}
}
