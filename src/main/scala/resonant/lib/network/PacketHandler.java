package resonant.lib.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import resonant.lib.References;
import resonant.lib.utility.nbt.ISaveObj;
import universalelectricity.api.vector.IVector2;
import universalelectricity.api.vector.IVector3;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/** Handles the packets.
 * 
 * @author Calclavia */
public class PacketHandler implements IPacketHandler
{
    public static final ArrayList<PacketType> registeredPackets = new ArrayList<PacketType>();

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        try
        {
            ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
            int packetID = data.readByte();
            EntityPlayer entityPlayer = (EntityPlayer) player;
            registeredPackets.get(packetID).receivePacket(data, entityPlayer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
                else if (dataValue instanceof IVector3)
                {
                    data.writeDouble(((IVector3) dataValue).x());
                    data.writeDouble(((IVector3) dataValue).y());
                    data.writeDouble(((IVector3) dataValue).z());
                }
                else if (dataValue instanceof IVector2)
                {
                    data.writeDouble(((IVector2) dataValue).x());
                    data.writeDouble(((IVector2) dataValue).y());
                }
                else if (dataValue instanceof NBTTagCompound)
                {
                    writeNBTTagCompound((NBTTagCompound) dataValue, data);
                }
                else if (dataValue instanceof FluidTank)
                {
                    data.writeInt(((FluidTank) dataValue).getCapacity());
                    writeNBTTagCompound(((FluidTank) dataValue).writeToNBT(new NBTTagCompound()), data);
                }
                else if (dataValue instanceof ISaveObj)
                {
                    NBTTagCompound nbt = new NBTTagCompound();
                    ((ISaveObj) dataValue).save(nbt);
                    writeNBTTagCompound(nbt, data);
                }
                else
                {
                    References.LOGGER.severe("Resonant Engine packet attempt to write an invalid type: " + dataValue.getClass());
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Packet data encoding failed.");
            e.printStackTrace();
        }
    }

    /** Reads a compressed NBTTagCompound from the InputStream */
    public static NBTTagCompound readNBTTagCompound(DataInput reader) throws IOException
    {
        // added null check
        Short short1 = reader.readShort();

        if (short1 == null || short1 < 0)
        {
            return null;
        }
        else
        {
            byte[] abyte = new byte[short1];
            reader.readFully(abyte);
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

    /** Sends packets to clients around a specific coordinate. A wrapper using Vector3. See
     * {@PacketDispatcher} for detailed information. */
    public static void sendPacketToClients(Packet packet, World worldObj, IVector3 position, double range)
    {
        try
        {
            PacketDispatcher.sendPacketToAllAround(position.x(), position.y(), position.z(), range, worldObj.provider.dimensionId, packet);
        }
        catch (Exception e)
        {
            System.out.println("Sending packet to client failed.");
            e.printStackTrace();
        }
    }

    /** Sends a packet to all the clients on this server. */
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
