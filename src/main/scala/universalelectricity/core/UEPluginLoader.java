package universalelectricity.core;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import universalelectricity.compatibility.asm.UniversalTransformer;

import java.util.Map;

/**
 * @author Calclavia
 */
@IFMLLoadingPlugin.TransformerExclusions({ "universalelectricity" })
public class UEPluginLoader implements IFMLLoadingPlugin, IFMLCallHook
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { UniversalTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return UEPluginLoader.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data)
	{

	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public Void call() throws Exception
	{
		/*
		val asmTETiles: String = System.getProperty("asmTETile")
		val asmBCTiles: String = System.getProperty("asmBCTile")
		val diable: String = System.getProperty("asmUEDsiable")

		if (asmTETiles == null || asmTETiles.equalsIgnoreCase("true") || asmTETiles.equalsIgnoreCase("t"))
		{
		  if (asmTETiles == null || asmTETiles.equalsIgnoreCase("true") || asmTETiles.equalsIgnoreCase("t")) TemplateInjectionManager.registerTileTemplate(CompatibilityType.THERMAL_EXPANSION.moduleName, classOf[TemplateTETile], classOf[IEnergyHandler])
		  if (asmBCTiles == null || asmBCTiles.equalsIgnoreCase("true") || asmBCTiles.equalsIgnoreCase("t")) TemplateInjectionManager.registerTileTemplate(CompatibilityType.BUILDCRAFT.moduleName, classOf[TemplateBCTile], classOf[IPowerReceptor])
		  TemplateInjectionManager.registerItemTemplate(CompatibilityType.THERMAL_EXPANSION.moduleName, classOf[TemplateTEItem], classOf[IEnergyContainerItem])
		}*/

		return null;
	}
}
