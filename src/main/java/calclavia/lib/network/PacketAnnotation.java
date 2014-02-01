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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * Packet handler for annotated objects.
 * 
 * @author Calclavia
 */
public class PacketAnnotation extends PacketType
{
	static int maxID = 0;

	/**
	 * A map of sync classes with their correspoding ID.
	 */
	protected static final BiMap<Class, Integer> classPacketIDMap = HashBiMap.create();

	/**
	 * Class ID : List of PacketSet sorted by ID
	 */
	protected static final HashMap<Integer, HashMap<Integer, PacketSet>> packetSetIDMap = new HashMap<Integer, HashMap<Integer, PacketSet>>();

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

		HashMap<Integer, PacketSet> packetSets = new HashMap<Integer, PacketSet>();

		for (Field f : clazz.getDeclaredFields())
		{
			if (f.isAnnotationPresent(Synced.class))
			{
				Synced sync = f.getAnnotation(Synced.class);

				for (int packetID : sync.id())
				{
					PacketSet packetSet = null;

					if (packetSets.containsKey(packetID))
					{
						packetSet = packetSets.get(packetID);
					}
					else
					{
						packetSet = new PacketSet(packetID);
					}

					packetSet.syncFields.add(f);
					packetSets.put(packetID, packetSet);
				}

				System.out.println("Annotation Packet Detected for: " + f.getName());
			}
		}

		packetSetIDMap.put(classID, packetSets);
	}

	public void sync(Object obj, int packetSetID)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			PacketDispatcher.sendPacketToAllPlayers(getPacket(obj, packetSetID));
		}
		else
		{
			PacketDispatcher.sendPacketToServer(getPacket(obj, packetSetID));
		}
	}

	public Packet getPacket(Object obj)
	{
		return getPacket(obj, 0);
	}

	public Packet getPacket(Object obj, int packetSetID)
	{
		constructPacketSets(obj.getClass());

		int classID = classPacketIDMap.get(obj.getClass());
		PacketSet packetSet = packetSetIDMap.get(classID).get(packetSetID);

		List args = packetSet.getPacketArray(obj);

		args.add(0, classID);
		args.add(1, packetSetID);

		if (obj instanceof TileEntity)
		{
			args.add(2, ((TileEntity) obj).xCoord);
			args.add(3, ((TileEntity) obj).yCoord);
			args.add(4, ((TileEntity) obj).zCoord);
		}

		return super.getPacket(args.toArray());
	}

	@Override
	public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		int classID = data.readInt();
		int packetSetID = data.readInt();

		try
		{
			if (TileEntity.class.isAssignableFrom(classPacketIDMap.inverse().get(classID)))
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
				packetSetIDMap.get(classID).get(packetSetID).read(tile, data);

				if (tile instanceof IPacketReceiver)
				{
					((IPacketReceiver) tile).onReceivePacket(data, player, x, y, z);
				}
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

					if (type == Integer.class || type == Integer.TYPE)
					{
						result = data.readInt();
					}
					else if (type == Float.class || type == Float.TYPE)
					{
						result = data.readFloat();
					}
					else if (type == Double.class || type == Double.TYPE)
					{
						result = data.readDouble();
					}
					else if (type == Byte.class || type == Byte.TYPE)
					{
						result = data.readByte();
					}
					else if (type == Boolean.class || type == Boolean.TYPE)
					{
						result = data.readBoolean();
					}
					else if (type == Short.class || type == Short.TYPE)
					{
						result = data.readShort();
					}
					else if (type == Long.class || type == Long.TYPE)
					{
						result = data.readLong();
					}
					else if (type == String.class)
					{
						result = data.readUTF();
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
