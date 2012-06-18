package net.minecraft.src.universalelectricity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;

public class UEPacketManager implements IPacketHandler
{
	public int id;
	public String channelName;
	public List<UEIPacketReceiver> packetUsers = new ArrayList();
	
	public UEPacketManager(String channelName)
	{
		this.channelName = channelName;
	}
	
	public void registerPacketUser(UEIPacketReceiver packetUser)
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
        
		for(UEIPacketReceiver packetUser : packetUsers)
		{
			if(packetUser.getPacketID() == packetID)
			{
				packetUser.onPacketData(network, channel, data);
			}
		}
	}
}
