package resonant.lib.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

/** @author Calclavia */
public abstract class PacketType
{
    public final String channel;
    public final byte id;

    public PacketType(String channel)
    {
        this.id = (byte) PacketHandler.registeredPackets.size();
        PacketHandler.registeredPackets.add(this);
        this.channel = channel;
    }

    protected Packet getPacket(Object... arg)
    {
        try
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream data = new DataOutputStream(bytes);

            PacketHandler.writeData(data, this.id);
            PacketHandler.writeData(data, arg);

            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = channel;
            packet.data = bytes.toByteArray();
            packet.length = packet.data.length;

            return packet;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void receivePacket(ByteArrayDataInput data, EntityPlayer player);
}
