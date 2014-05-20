package universalelectricity.api;

import java.util.LinkedHashSet;
import java.util.Set;

import universalelectricity.compatibility.ModuleBuildCraft;
import universalelectricity.compatibility.ModuleThermalExpansion;

import cpw.mods.fml.common.Loader;

/**
 * ORDER OF MAGNITUDE:
 * A coal in Universal Electricity (based on an estimate in real life) is worth 4MJ.
 * A fission reactor should make around 4-9GW.
 * A fusion reactor would go into the tera-watts.
 * Reika's conversion: IC2[22512], BC[56280], RF[5628]
 * @author Calclavia
 */
public class CompatibilityType
{
	private static Set<CompatibilityType> modules = new LinkedHashSet<CompatibilityType>();

	public static final CompatibilityType BUILDCRAFT = new CompatibilityType("BuildCraft|Energy", "BuildCraft", "Minecraft Joule", "MJ", 500, ModuleBuildCraft.class);
	public static final CompatibilityType THERMAL_EXPANSION = new CompatibilityType("ThermalExpansion", "ThermalExpansion", "Redstone Flux", "RF", 50, ModuleThermalExpansion.class);
	
	static
	{
		register(BUILDCRAFT);
		register(THERMAL_EXPANSION);
	}

	public final String modID;
	public final String moduleName;
	public final String fullUnit;
	public final String unit;

	/**
	 * Multiply UE energy by this ratio to convert it to the forgien ratio.
	 */
	public double ratio;

	/**
	 * Multiply the forgien energy by this ratio to convert it into UE energy.
	 */
	public double reciprocal_ratio;

	/**
	 * The Universal Electricity Loader will change this value to indicate if the module is
	 * loaded or not.
	 */
	public boolean isModuleEnabled;

	public Class<? extends CompatibilityModule> moduleClass;

	/**
	 * @param modID - The Forge mod ID.
	 * @param moduleName - The name of the module, used for config and ASM
	 * @param fullUnit - The unit used
	 * @param unit - The unit short form used
	 * @param ratio - How much UE energy equates to the forgien energy?
	 */
	CompatibilityType(String modID, String moduleName, String fullUnit, String unit, int ratio, Class<? extends CompatibilityModule> moduleClass)
	{
		this.modID = modID;
		this.moduleName = moduleName;
		this.fullUnit = fullUnit;
		this.unit = unit;
		this.ratio = 1.0 / ratio;
		this.reciprocal_ratio = ratio;
		this.moduleClass = moduleClass;
	}

	public boolean isLoaded()
	{
		return Loader.isModLoaded(this.modID);
	}

	public static CompatibilityType get(String moduleName)
	{
		for (CompatibilityType type : getModules().toArray(new CompatibilityType[0]))
		{
			if (moduleName.equals(type.moduleName))
			{
				return type;
			}
		}

		return null;
	}

	public static Set<CompatibilityType> getModules() 
	{
		return modules;
	}

	private static void register(CompatibilityType compatibilityType) 
	{
		modules.add(compatibilityType);

	}
}
