package calclavia.lib.network;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.Calclavia;
import calclavia.lib.network.Synced.SyncedInput;
import calclavia.lib.network.Synced.SyncedOutput;
import calclavia.lib.utility.nbt.ISaveObj;

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

	public static final List<String> syncedClasses = new ArrayList<String>();

	public PacketAnnotation(String channel)
	{
		super(channel);
	}

	/**
	 * Constructs the packet sets for this specific class
	 */
	public static void constructPacketSets(Class clazz)
	{
		if (classPacketIDMap.containsKey(clazz))
		{
			return;
		}

		int classID = ++maxID;
		classPacketIDMap.put(clazz, classID);

		HashMap<Integer, PacketSet> packetSets = new HashMap<Integer, PacketSet>();

		while (clazz != null)
		{
			for (Field f : clazz.getDeclaredFields())
			{
				if (f.isAnnotationPresent(Synced.class))
				{
					Synced sync = f.getAnnotation(Synced.class);

					for (int packetID : sync.value())
					{
						PacketSet packetSet = packetSets.containsKey(packetID) ? packetSets.get(packetID) : new PacketSet(packetID);
						packetSet.syncFields.add(f);
						packetSets.put(packetID, packetSet);
					}
				}
			}

			for (Method m : clazz.getDeclaredMethods())
			{
				if (m.isAnnotationPresent(SyncedInput.class))
				{
					SyncedInput sync = m.getAnnotation(SyncedInput.class);

					for (int packetID : sync.value())
					{
						PacketSet packetSet = packetSets.containsKey(packetID) ? packetSets.get(packetID) : new PacketSet(packetID);
						packetSet.syncInputs.add(m);
						packetSets.put(packetID, packetSet);
					}
				}

				if (m.isAnnotationPresent(SyncedOutput.class))
				{
					SyncedOutput sync = m.getAnnotation(SyncedOutput.class);

					for (int packetID : sync.value())
					{
						PacketSet packetSet = packetSets.containsKey(packetID) ? packetSets.get(packetID) : new PacketSet(packetID);
						packetSet.syncOutputs.add(m);
						packetSets.put(packetID, packetSet);
					}
				}
			}

			clazz = clazz.getSuperclass();
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
		Iterator<String> it = syncedClasses.iterator();

		while (it.hasNext())
		{
			try
			{
				constructPacketSets(Class.forName(it.next()));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}

			it.remove();
		}

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

				if (tile != null)
				{
					packetSetIDMap.get(classID).get(packetSetID).read(tile, data);

					if (tile instanceof IPacketReceiver)
					{
						((IPacketReceiver) tile).onReceivePacket(data, player, x, y, z);
					}
				}
				else
				{
					Calclavia.LOGGER.severe("Annotation packet sent to null: " + x + ", " + y + ", " + z);
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

	public static class PacketSet
	{
		public final int id;

		public final List<Field> syncFields = new ArrayList<Field>();
		public final List<Method> syncInputs = new ArrayList<Method>();
		public final List<Method> syncOutputs = new ArrayList<Method>();

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

				for (Method m : syncOutputs)
				{
					m.setAccessible(true);
					NBTTagCompound nbt = new NBTTagCompound();
					m.invoke(obj, nbt);
					args.add(nbt);
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
					else if (type == FluidTank.class)
					{
						result = new FluidTank(data.readInt()).readFromNBT(PacketHandler.readNBTTagCompound(data));
					}
					else if (ISaveObj.class.isAssignableFrom(type))
					{
						result = f.get(obj);
						((ISaveObj) result).load(PacketHandler.readNBTTagCompound(data));
					}

					if (result == null)
					{
						Calclavia.LOGGER.severe("Calclavia packet read a null field for " + obj.getClass().getSimpleName());
					}

					f.set(obj, result);
				}

				for (Method m : syncInputs)
				{
					m.setAccessible(true);
					m.invoke(obj, PacketHandler.readNBTTagCompound(data));
				}
			}
			catch (Exception e)
			{
				Calclavia.LOGGER.severe("Calclavia annotation packet failed for " + obj.getClass().getSimpleName());
				e.printStackTrace();
			}
		}
	}
}
