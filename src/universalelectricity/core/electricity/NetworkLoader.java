package universalelectricity.core.electricity;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import universalelectricity.core.block.IConductor;
import cpw.mods.fml.common.FMLLog;

@SuppressWarnings("unchecked")
public class NetworkLoader
{
	/**
	 * The default IElectricityNetwork used for primary electrical networks.
	 */
	public static Class<? extends IElectricityNetwork> NETWORK_CLASS;
	public static final Set<Class<? extends IElectricityNetwork>> NETWORK_CLASS_REGISTRY = new HashSet<Class<? extends IElectricityNetwork>>();

	static
	{
		setNetworkClass("universalelectricity.core.electricity.ElectricityNetwork");
		MinecraftForge.EVENT_BUS.register(new NetworkLoader());
	}

	public static void setNetworkClass(Class<? extends IElectricityNetwork> networkClass)
	{
		NETWORK_CLASS_REGISTRY.add(networkClass);
		NETWORK_CLASS = networkClass;
	}

	public static void setNetworkClass(String className)
	{
		try
		{
			NETWORK_CLASS = (Class<? extends IElectricityNetwork>) Class.forName(className);
		}
		catch (Exception e)
		{
			FMLLog.severe("Universal Electricity: Failed to set network class with name " + className);
			e.printStackTrace();
		}
	}

	public static IElectricityNetwork getNewNetwork()
	{
		try
		{
			return NETWORK_CLASS.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@ForgeSubscribe
	public void onChunkLoad(ChunkEvent.Load event)
	{
		if (event.getChunk() != null)
		{
			for (Object obj : event.getChunk().chunkTileEntityMap.values())
			{
				if (obj instanceof TileEntity)
				{
					TileEntity tileEntity = (TileEntity) obj;

					if (tileEntity instanceof IConductor)
					{
						((IConductor) tileEntity).updateAdjacentConnections();
					}
				}
			}
		}
	}
}