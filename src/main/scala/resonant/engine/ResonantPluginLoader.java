package resonant.engine;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import resonant.engine.asm.Transformer;

import java.util.Map;

/**
 * The FMLLoadingPlugin for Resonant Engine including the ASM Transformer
 *
 * @author Calclavia
 */

@TransformerExclusions({ "resonant.engine.asm" })
public class ResonantPluginLoader implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { Transformer.class.getName() };
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

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
