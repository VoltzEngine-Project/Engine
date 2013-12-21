package universalelectricity.api;

import cpw.mods.fml.common.Loader;

/**
 * ORDER OF MAGNITUDE:
 * A coal in Universal Electricity (based on an estimate in real life) is worth 4MJ.
 * A fission reactor should make around 4-9GW.
 * A fusion reactor would go into the tera-watts.
 * 
 * @author Calclavia
 */
public class Compatibility
{
	public static enum CompatibilityType
	{
		THERMAL_EXPANSION("ThermalExpansion", "Redstone Flux", "RF", 281.4f),
		INDUSTRIALCRAFT("IC2", "Electrical Unit", "EU", 112.56f),
		BUILDCRAFT("BuildCraft|Energy", "Minecraft Joule", "MJ", 28.14f);

		public final String modID;

		public final String fullUnit;

		public final String unit;

		/**
		 * Multiply UE energy by this ratio to convert it to the forgien ratio.
		 */
		public float ratio;

		/**
		 * Multiply the forgien energy by this ratio to convert it into UE energy.
		 */
		public float reciprocal_ratio;

		CompatibilityType(String modID, String fullUnit, String unit, float ratio)
		{
			this.modID = modID;
			this.fullUnit = fullUnit;
			this.unit = unit;
			this.ratio = ratio;
			this.reciprocal_ratio = 1 / this.ratio;
		}

		public boolean isLoaded()
		{
			return Loader.isModLoaded(this.modID);
		}
	}

}