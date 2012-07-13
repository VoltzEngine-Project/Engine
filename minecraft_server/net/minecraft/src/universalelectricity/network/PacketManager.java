package net.minecraft.src.universalelectricity.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;

public class PacketManager implements IPacketHandler
{
    public int id;
    public String channelName;

    public PacketManager(String channelName)
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

    public void sendPacketData(IPacketSender sender, double[] otherData)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(sender.getPacketID());

            //If it's a tile entity, send it's coordinates
            if (sender instanceof TileEntity)
            {
                data.writeInt(((TileEntity)sender).xCoord);
                data.writeInt(((TileEntity)sender).yCoord);
                data.writeInt(((TileEntity)sender).zCoord);
            }
            else
            {
                data.writeInt(0);
                data.writeInt(0);
                data.writeInt(0);
            }

            for (double dataValue : otherData)
            {
                data.writeDouble(dataValue);
            }
        }
        catch (IOException e)
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
