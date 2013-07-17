package universalelectricity.compatibility;

import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.NetworkLoader;
import cpw.mods.fml.common.Loader;

public class Compatibility
{
	/**
	 * Multiply this to convert foreign energy into UE Joules.
	 */
	public static float IC2_RATIO = 50;
	public static float BC3_RATIO = 125;
	/**
	 * Multiply this to convert UE Joules into foreign energy.
	 */
	public static float TO_IC2_RATIO = 1 / IC2_RATIO;
	public static float TO_BC_RATIO = 1 / BC3_RATIO;

	public static void initiate()
	{
		/**
		 * Loads the configuration and sets all the values.
		 */
		UniversalElectricity.CONFIGURATION.load();
		IC2_RATIO = (float) UniversalElectricity.CONFIGURATION.get("Compatiblity", "IndustrialCraft Conversion Ratio", IC2_RATIO).getDouble(IC2_RATIO);
		BC3_RATIO = (float) UniversalElectricity.CONFIGURATION.get("Compatiblity", "BuildCraft Conversion Ratio", BC3_RATIO).getDouble(BC3_RATIO);
		TO_IC2_RATIO = 1 / IC2_RATIO;
		TO_BC_RATIO = 1 / BC3_RATIO;
		UniversalElectricity.CONFIGURATION.save();

		NetworkLoader.setNetworkClass(UniversalNetwork.class);
	}

	public static boolean isIndustrialCraft2Loaded()
	{
		return Loader.isModLoaded("IC2");
	}

	public static boolean isBuildcraftLoaded()
	{
		return Loader.isModLoaded("BuildCraft|Energy");
	}
}
