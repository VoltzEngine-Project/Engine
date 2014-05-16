package resonant.core;

import java.util.Map;

import resonant.core.asm.Transformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

/** The FMLLoadingPlugin for Resonant Engine including the ASM Transformer
 * 
 * @author Calclavia */

@TransformerExclusions({ "resonant.core.asm" })
public class ResonantPluginLoader implements IFMLLoadingPlugin
{

    public String[] getLibraryRequestClass()
    {
        return null;
    }

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
}
