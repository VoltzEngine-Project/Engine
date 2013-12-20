package universalelectricity.core.grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import universalelectricity.api.energy.IEnergyConductor;
import cpw.mods.fml.common.FMLLog;

@SuppressWarnings("unchecked")
public class NetworkLoader
{
	/**
	 * The default IElectricityNetwork used for primary electrical networks.
	 */
	public static Class<? extends IEnergyNetwork> NETWORK_CLASS;
	public static final Set<Class<? extends IEnergyNetwork>> NETWORK_CLASS_REGISTRY = new HashSet<Class<? extends IEnergyNetwork>>();

	static
	{
		setNetworkClass("universalelectricity.core.grid.ElectricityNetwork");
	}

	public static void setNetworkClass(Class<? extends IEnergyNetwork> networkClass)
	{
		NETWORK_CLASS_REGISTRY.add(networkClass);
		NETWORK_CLASS = networkClass;
	}

	public static void setNetworkClass(String className)
	{
		try
		{
			setNetworkClass((Class<? extends IEnergyNetwork>) Class.forName(className));
		}
		catch (Exception e)
		{
			FMLLog.severe("Universal Electricity: Failed to set network class with name " + className);
			e.printStackTrace();
		}
	}

	public static IEnergyNetwork getNewNetwork(IEnergyConductor... conductors)
	{
		try
		{
			IEnergyNetwork network = NETWORK_CLASS.newInstance();
			network.getConnectors().addAll(Arrays.asList(conductors));
			return network;
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

}