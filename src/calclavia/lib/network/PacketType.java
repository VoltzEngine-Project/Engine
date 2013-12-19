package calclavia.lib.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

/** @author Calclavia */
public abstract class PacketType
{
	public final String channel;
	public final int id;

	public PacketType(String channel)
	{
		this.id = PacketHandler.registeredPackets.size();
		PacketHandler.registeredPackets.add(this);
		this.channel = channel;
	}

	public Packet getPacket(Object... arg)
	{
		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream data = new DataOutputStream(bytes);

			this.writeData(data, this.id);

			for (Object obj : arg)
			{
				this.writeData(data, obj);
			}

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

	public static void writeData(DataOutputStream data, Object... sendData)
	{
		try
		{
			for (Object dataValue : sendData)
			{
				if (dataValue instanceof Integer)
				{
					data.writeInt((Integer) dataValue);
				}
				else if (dataValue instanceof Float)
				{
					data.writeFloat((Float) dataValue);
				}
				else if (dataValue instanceof Double)
				{
					data.writeDouble((Double) dataValue);
				}
				else if (dataValue instanceof Byte)
				{
					data.writeByte((Byte) dataValue);
				}
				else if (dataValue instanceof Boolean)
				{
					data.writeBoolean((Boolean) dataValue);
				}
				else if (dataValue instanceof String)
				{
					data.writeUTF((String) dataValue);
				}
				else if (dataValue instanceof Short)
				{
					data.writeShort((Short) dataValue);
				}
				else if (dataValue instanceof Long)
				{
					data.writeLong((Long) dataValue);
				}
				else if (dataValue instanceof NBTTagCompound)
				{
					PacketHandler.writeNBTTagCompound((NBTTagCompound) dataValue, data);
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Packet data encoding failed.");
			e.printStackTrace();
		}
	}

	public abstract void receivePacket(ByteArrayDataInput data, EntityPlayer player);
}
