package universalelectricity.core;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ISpecialElectricItem;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.Configuration;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.energy.EnergyNetworkLoader;
import universalelectricity.compatibility.ModuleUniversalElectricity;
import universalelectricity.core.asm.TemplateInjectionManager;
import universalelectricity.core.asm.UniversalTransformer;
import universalelectricity.core.asm.template.item.TemplateICItem;
import universalelectricity.core.asm.template.item.TemplateTEItem;
import universalelectricity.core.asm.template.tile.TemplateICTile;
import universalelectricity.core.asm.template.tile.TemplateTETile;
import universalelectricity.core.net.EnergyNetwork;
import universalelectricity.core.net.NetworkTickHandler;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "UniversalElectricity", version = UniversalElectricity.VERSION, name = "Universal Electricity", dependencies = "before:ForgeMultipart")
@TransformerExclusions({ "universalelectricity.core.asm", "universalelectricity.core.asm.template" })
public class UELoader implements IFMLLoadingPlugin
{
	static
	{
		TemplateInjectionManager.registerTileTemplate(CompatibilityType.THERMAL_EXPANSION.moduleName, TemplateTETile.class, IEnergyHandler.class);
		TemplateInjectionManager.registerTileTemplate(CompatibilityType.INDUSTRIALCRAFT.moduleName, TemplateICTile.class, IEnergySink.class, IEnergySource.class);

		TemplateInjectionManager.registerItemTemplate(CompatibilityType.THERMAL_EXPANSION.moduleName, TemplateTEItem.class, IEnergyContainerItem.class);
		TemplateInjectionManager.registerItemTemplate(CompatibilityType.INDUSTRIALCRAFT.moduleName, TemplateICItem.class, ISpecialElectricItem.class);
	}

	/**
	 * The Universal Electricity configuration file.
	 */
	public static Configuration CONFIGURATION;

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity.cfg"));
		/** Loads the configuration and sets all the values. */
		CONFIGURATION.load();
		CompatibilityType.THERMAL_EXPANSION.ratio = (float) CONFIGURATION.get("Compatiblity", "Thermal Expansion Conversion Ratio", CompatibilityType.THERMAL_EXPANSION.ratio).getDouble(CompatibilityType.THERMAL_EXPANSION.ratio);
		CompatibilityType.INDUSTRIALCRAFT.ratio = (float) CONFIGURATION.get("Compatiblity", "IndustrialCraft Conversion Ratio", CompatibilityType.INDUSTRIALCRAFT.ratio).getDouble(CompatibilityType.INDUSTRIALCRAFT.ratio);
		CompatibilityType.BUILDCRAFT.ratio = (float) CONFIGURATION.get("Compatiblity", "BuildCraft Conversion Ratio", CompatibilityType.BUILDCRAFT.ratio).getDouble(CompatibilityType.BUILDCRAFT.ratio);

		CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio = 1 / CompatibilityType.THERMAL_EXPANSION.ratio;
		CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio = 1 / CompatibilityType.INDUSTRIALCRAFT.ratio;
		CompatibilityType.BUILDCRAFT.reciprocal_ratio = 1 / CompatibilityType.BUILDCRAFT.ratio;

		CompatibilityModule.register(new ModuleUniversalElectricity());

		for (CompatibilityType compatibility : CompatibilityType.values())
		{
			compatibility.isModuleEnabled = CONFIGURATION.get("Compatiblity", "Load " + compatibility.moduleName + " Module", true).getBoolean(true);

			if (compatibility.isModuleEnabled)
			{
				try
				{
					CompatibilityModule.register((CompatibilityModule) Class.forName("universalelectricity.compatibility.Module" + compatibility.moduleName).newInstance());
				}
				catch (Exception e)
				{
					System.out.println("[Universal Electricity] Failed to load module: " + compatibility.moduleName);
					e.printStackTrace();
				}
			}
		}

		CONFIGURATION.save();

		TickRegistry.registerTickHandler(NetworkTickHandler.INSTANCE, Side.SERVER);
		EnergyNetworkLoader.setNetworkClass(EnergyNetwork.class);
	}

	/**
	 * Return a list of classes that implements the IClassTransformer interface
	 * 
	 * @return a list of classes that implements the IClassTransformer interface
	 */
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { UniversalTransformer.class.getName() };
	}

	/**
	 * Return a class name that implements "ModContainer" for injection into the mod list The
	 * "getName" function should return a name that other mods can, if need be, depend on.
	 * Trivially, this modcontainer will be loaded before all regular mod containers, which means it
	 * will be forced to be "immutable" - not susceptible to normal sorting behaviour. All other mod
	 * behaviours are available however- this container can receive and handle normal loading events
	 */
	@Override
	public String getModContainerClass()
	{
		return null;
	}

	/**
	 * Return the class name of an implementor of "IFMLCallHook", that will be run, in the main
	 * thread, to perform any additional setup this coremod may require. It will be run
	 * <strong>prior</strong> to Minecraft starting, so it CANNOT operate on minecraft itself. The
	 * game will deliberately crash if this code is detected to trigger a minecraft class loading
	 * (TODO: implement crash ;) )
	 */
	@Override
	public String getSetupClass()
	{
		return null;
	}

	/**
	 * Inject coremod data into this coremod This data includes: "mcLocation" : the location of the
	 * minecraft directory, "coremodList" : the list of coremods "coremodLocation" : the file this
	 * coremod loaded from,
	 */
	@Override
	public void injectData(Map<String, Object> data)
	{
	}

	public String[] getLibraryRequestClass()
	{
		return null;
	}
}