package calclavia.lib.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

/**
 * The FMLLoadingPlugin for Calclavia Core
 * 
 * @author Calclavia
 * 
 */
@TransformerExclusions({ "calclavia.lib.asm" })
public class CalclaviaCoreLoader implements IFMLLoadingPlugin
{
	public String[] getLibraryRequestClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { CalclaviaTransformer.class.getName() };
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
