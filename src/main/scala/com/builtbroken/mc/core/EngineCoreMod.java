package com.builtbroken.mc.core;

import com.builtbroken.mc.core.asm.template.InjectionTemplate;
import com.builtbroken.mc.core.deps.DepDownloader;
import com.builtbroken.mc.lib.mod.compat.rf.TemplateTETile;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Core mod for Voltz Engine handing anything that needs to be done before mods load.
 * Created by Dark on 9/7/2015.
 * -Dfml.coreMods.load=com.builtbroken.mc.core.EngineCoreMod
 */
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class EngineCoreMod implements IFMLLoadingPlugin
{
    /** List of ASM injection templates to use on universal energy tiles */
    public static final HashMap<String, InjectionTemplate> templates = new HashMap();

    public EngineCoreMod()
    {
        //TODO see if there is a better place to load this as a construct is not designed for downloading
        if(System.getProperty("development") == null || !System.getProperty("development").equalsIgnoreCase("true"))
        {
            DepDownloader.load();
        }

        try
        {
            if (Class.forName("cofh.api.energy.IEnergyHandler") != null)
            {
                templates.put("RF-IEnergyHandler", new InjectionTemplate(TemplateTETile.class.getName(), Collections.singletonList("cofh.api.energy.IEnergyHandler")));
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{"com.builtbroken.mc.core.asm.ChunkTransformer", "com.builtbroken.mc.core.asm.template.ClassTransformer"};
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
