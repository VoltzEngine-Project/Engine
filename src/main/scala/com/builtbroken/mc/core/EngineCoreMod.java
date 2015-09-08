package com.builtbroken.mc.core;

import com.builtbroken.mc.core.deps.DepDownloader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Core mod for Voltz Engine handing anything that needs to be done before mods load.
 * Created by Dark on 9/7/2015.
 * -Dfml.coreMods.load=com.builtbroken.mc.core.EngineCoreMod
 */
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class EngineCoreMod implements IFMLLoadingPlugin
{
    public EngineCoreMod()
    {
        //TODO see if there is a better place to load this as a construct is not designed for downloading
        DepDownloader.load();
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return "com.builtbroken.mc.core.EnginePreloader";
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
