package net.minecraft.src.universalelectricity.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;

public class PacketManager implements IPacketHandler
{
	public int id;
	public String channelName;
	public List<IPacketReceiver> packetUsers = new ArrayList();
	
	/**
	 * @param channelName - Channel name can not be longer than 16 characters
	 */
	public PacketManager(String channelName)
	{
		this.channelName = channelName;
	}
	
	public void registerPacketUser(IPacketReceiver packetUser)
	{
		if(!this.packetUsers.contains(packetUser))
		{
			this.packetUsers.add(packetUser);
		}
	}
	
	public void registerChannel(NetworkManager network)
	{
		MessageManager.getInstance().registerChannel(network, this, this.channelName);
	}
	
	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] data)
	{
		DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));
		
        int packetID = -1;
        int[] packetData = new int[1];
        
        try
        {
        	packetID = dataStream.readInt();
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
        
		for(IPacketReceiver packetUser : packetUsers)
		{
			if(packetUser.getPacketID() == packetID)
			{
				packetUser.onPacketData(network, channel, data);
			}
		}
	}
}
