package universalelectricity.api;

import cpw.mods.fml.common.Loader;

/**
 * @author Calclavia
 */
public class Compatibility
{
	public static enum CompatibilityType
	{
		THERMAL_EXPANSION("ThermalExpansion", 281.4f), INDUSTRIALCRAFT("IC2", 112.56f),
		BUILDCRAFT("BuildCraft|Energy", 28.14f);

		public final String modID;

		/**
		 * Multiply UE energy by this ratio to convert it to the forgien ratio.
		 */
		public float ratio;

		/**
		 * Multiply the forgien energy by this ratio to convert it into UE energy.
		 */
		public float reciprocal_ratio;

		CompatibilityType(String modID, float ratio)
		{
			this.modID = modID;
			this.ratio = ratio;
			this.reciprocal_ratio = 1 / this.ratio;
		}

		public boolean isLoaded()
		{
			return Loader.isModLoaded(this.modID);
		}
	}
	
	
}