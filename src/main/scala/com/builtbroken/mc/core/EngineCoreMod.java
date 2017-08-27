package com.builtbroken.mc.core;

import com.builtbroken.mc.core.deps.DepDownloader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Core mod for Voltz Engine handing anything that needs to be done before mods load.
 * Created by Dark on 9/7/2015.
 * -Dfml.coreMods.load=com.builtbroken.mc.core.EngineCoreMod
 */
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class EngineCoreMod implements IFMLLoadingPlugin
{
    /** Grab the mod's main logger, in theory should be the same logger */
    public static final Logger logger = LogManager.getLogger("VoltzEngine");
    public static boolean devMode = false;

    /** Check to do downloads if enabled */
    public static boolean doDownloads;
    /** Check to only run ASM if enabled */
    public static boolean enableASM;

    public EngineCoreMod()
    {
        //TODO see if there is a better place to load this as a construct is not designed for downloading
        final boolean notDevMode = System.getProperty("development") == null || !System.getProperty("development").equalsIgnoreCase("true");
        doDownloads = System.getProperty("disableDepDownloader") == null || !System.getProperty("disableDepDownloader").equalsIgnoreCase("true");
        enableASM = System.getProperty("enableAsm") == null || System.getProperty("enableAsm").equalsIgnoreCase("true");

        devMode = !notDevMode;

        if (notDevMode && doDownloads)
        {
            DepDownloader.load();
        }
        if (enableASM)
        {
            //TemplateManager.load();
        }
    }

    @Override
    public String[] getASMTransformerClass()
    {
        if (enableASM)
        {
            return new String[]{"com.builtbroken.mc.core.asm.ChunkTransformer", "com.builtbroken.mc.core.asm.template.ClassTransformer"};
        }
        return new String[]{};
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
