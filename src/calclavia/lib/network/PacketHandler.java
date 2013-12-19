package calclavia.lib.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import universalelectricity.core.vector.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * Handles the packets.
 * 
 * @author Calclavia
 */
public class PacketHandler implements IPacketHandler
{
	public static final ArrayList<PacketType> registeredPackets = new ArrayList<PacketType>();

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		try
		{
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

			int packetID = data.readInt();
			EntityPlayer entityPlayer = (EntityPlayer) player;

			registeredPackets.get(packetID).receivePacket(data, entityPlayer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Reads a compressed NBTTagCompound from the InputStream */
	public static NBTTagCompound readNBTTagCompound(DataInput par0DataInput) throws IOException
	{
		short short1 = par0DataInput.readShort();

		if (short1 < 0)
		{
			return null;
		}
		else
		{
			byte[] abyte = new byte[short1];
			par0DataInput.readFully(abyte);
			return CompressedStreamTools.decompress(abyte);
		}
	}

	public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutput par1DataOutput) throws IOException
	{
		if (par0NBTTagCompound == null)
		{
			par1DataOutput.writeShort(-1);
		}
		else
		{
			byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);
			par1DataOutput.writeShort((short) abyte.length);
			par1DataOutput.write(abyte);
		}
	}

	/**
	 * Sends packets to clients around a specific coordinate. A wrapper using Vector3. See
	 * {@PacketDispatcher} for detailed information.
	 */
	public static void sendPacketToClients(Packet packet, World worldObj, Vector3 position, double range)
	{
		try
		{
			PacketDispatcher.sendPacketToAllAround(position.x, position.y, position.z, range, worldObj.provider.dimensionId, packet);
		}
		catch (Exception e)
		{
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}

	/**
	 * Sends a packet to all the clients on this server.
	 */
	public static void sendPacketToClients(Packet packet, World worldObj)
	{
		try
		{
			PacketDispatcher.sendPacketToAllInDimension(packet, worldObj.provider.dimensionId);
		}
		catch (Exception e)
		{
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}

	public static void sendPacketToClients(Packet packet)
	{
		try
		{
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
		catch (Exception e)
		{
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}
}
