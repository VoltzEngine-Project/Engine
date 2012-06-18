package net.minecraft.src.universalelectricity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;

public class UEPacketManager implements IPacketHandler
{
	public int id;
	public String channelName;
	
	public UEPacketManager(String channelName)
	{
		this.channelName = channelName;
	}
	
	public void registerChannel(NetworkManager network)
	{
		MessageManager.getInstance().registerChannel(network, this, this.channelName);
	}
	
	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] data)
	{
		
	}
	
	public void sendPacketData(int ID, double[] otherData)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        
        try
        {
        	data.writeInt(ID);
        	
        	for(double dataValue : otherData)
        	{
        		data.writeDouble(dataValue);
        	}
        }
        catch(IOException e)
        {
                e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = this.channelName;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        ModLoader.getMinecraftServerInstance().configManager.sendPacketToAllPlayers(packet);
	}
}
