package calclavia.lib;

import java.util.Map;

import calclavia.lib.asm.CalclaviaTransformer;
import calclavia.lib.config.ConfigTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

/**
 * The FMLLoadingPlugin for Calclavia Core including the ASM Transformer
 * 
 * @author Calclavia
 * 
 */

@TransformerExclusions({ "calclavia.lib.asm" })
public class CalclaviaPluginLoader implements IFMLLoadingPlugin
{
	public String[] getLibraryRequestClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { CalclaviaTransformer.class.getName(), ConfigTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{

	}
}
