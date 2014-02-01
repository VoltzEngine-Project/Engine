package calclavia.lib.network;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import universalelectricity.api.vector.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import calclavia.lib.Calclavia;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Packet handler for annotated objects.
 * 
 * @author Calclavia
 */
public class PacketAnnotation extends PacketType
{
	static int maxID = 0;
	protected static final BiMap<Class, Integer> classPacketIDMap = HashBiMap.create();
	protected static final HashMap<Integer, List<PacketSet>> packetSetIDMap = new HashMap<Integer, List<PacketSet>>();

	public PacketAnnotation(String channel)
	{
		super(channel);
	}

	/**
	 * Constructs the packet sets for this specific class
	 */
	public void constructPacketSets(Class clazz)
	{
		if (classPacketIDMap.containsKey(clazz))
		{
			return;
		}

		int classID = ++maxID;
		classPacketIDMap.put(clazz, classID);

		List<PacketSet> packetSets = new ArrayList<PacketSet>();

		for (Field f : clazz.getFields())
		{
			Synced sync = f.getAnnotation(Synced.class);

			if (sync != null)
			{
				for (int id : sync.id())
				{
					PacketSet packetSet = null;

					if (packetSets.get(id) != null)
					{
						packetSet = packetSets.get(id);
					}
					else
					{
						packetSet = new PacketSet(id);
					}

					packetSet.syncFields.add(f);
					packetSets.set(id, packetSet);
				}

				System.out.println("Annotation Packet Detected for: " + f.getName());
			}
		}
	}

	public void sync(Object obj, int packetSetID)
	{
		PacketDispatcher.sendPacketToAllPlayers(getPacket(obj, packetSetID));
	}

	public Packet getPacket(Object obj)
	{
		return getPacket(obj, 0);
	}

	public Packet getPacket(Object obj, int packetSetID)
	{
		if (!classPacketIDMap.containsKey(obj.getClass()))
		{
			constructPacketSets(obj.getClass());
		}

		int classID = classPacketIDMap.get(obj.getClass());
		List<PacketSet> packetSets = packetSetIDMap.get(classID);

		List args = packetSets.get(packetSetID).getPacketArray(obj);

		args.add(0, classID);
		args.add(1, packetSetID);

		return super.getPacket(args.toArray());
	}

	@Override
	public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		int classID = data.readInt();
		int packetSetID = data.readInt();
		try
		{
			if (classPacketIDMap.inverse().get(classID).isAssignableFrom(TileEntity.class))
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
				packetSetIDMap.get(classID).get(packetSetID).read(tile, data);
			}
			else
			{
				Calclavia.LOGGER.severe("Calclavia annotation packet failed to be read.");
			}
		}
		catch (Exception e)
		{
			Calclavia.LOGGER.severe("Calclavia packet failed with CLASS ID: " + classID);
			e.printStackTrace();
		}
	}

	class PacketSet
	{
		public final int id;

		public final List<Field> syncFields = new ArrayList<Field>();

		public PacketSet(int id)
		{
			this.id = id;
		}

		public List getPacketArray(Object obj)
		{
			List args = new ArrayList();

			try
			{
				for (Field f : syncFields)
				{
					f.setAccessible(true);
					args.add(f.get(obj));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return args;
		}

		public void read(Object obj, ByteArrayDataInput data)
		{
			try
			{
				for (Field f : syncFields)
				{
					f.setAccessible(true);
					Class type = f.getType();
					Object result = null;

					if (type == Integer.class)
					{
						result = data.readInt();
					}
					else if (type == Float.class)
					{
						result = data.readFloat();
					}
					else if (type == Double.class)
					{
						result = data.readDouble();
					}
					else if (type == Byte.class)
					{
						result = data.readByte();
					}
					else if (type == Boolean.class)
					{
						result = data.readBoolean();
					}
					else if (type == String.class)
					{
						result = data.readUTF();
					}
					else if (type == Short.class)
					{
						result = data.readShort();
					}
					else if (type == Long.class)
					{
						result = data.readLong();
					}
					else if (type == Vector3.class)
					{
						result = new Vector3(data.readDouble(), data.readDouble(), data.readDouble());
					}
					else if (type == NBTTagCompound.class)
					{
						result = PacketHandler.readNBTTagCompound(data);
					}

					if (result == null)
					{
						Calclavia.LOGGER.severe("Calclavia packet read a null field for " + obj.getClass().getSimpleName());
					}

					f.set(obj, result);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
