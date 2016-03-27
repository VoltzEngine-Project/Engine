package com.builtbroken.mc.lib.mod;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.mod.loadable.LoadableHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Default layout for a mod class to make it easier to keep mod.class
 * in the same general design and do the same general actions.
 * <p/>
 * You will still need to place @Mod at the top of the class, create your own proxies,
 * and do other tasks that can't be abstracted out due to @Annotations
 *
 * @Mod
 * @Instance
 * @SidedProxy
 * @EventHandler
 * @Mod.Metadata
 * @ModstatInfo Created by robert on 12/7/2014.
 */
public abstract class AbstractMod
{
    /** Loader handler for proxies and loadable objects */
    protected LoadableHandler loader;
    /** Manager for creating and handling content */
    protected ModManager manager;
    /** Info or error logger */
    protected Logger logger;
    /** Custom path to config file */
    protected String configPath;
    /** Configuration file */
    private Configuration config;
    /** Toggle to stop pre-init from firing in case extra handling needs to be done */
    protected boolean fireProxyPreInit = true;

    /**
     * @param domain - mod id uses to register textures with, etc
     */
    public AbstractMod(String domain)
    {
        loader = new LoadableHandler();
        manager = new ModManager().setPrefix(domain);
        logger = LogManager.getLogger(domain);
    }

    public AbstractMod(String domain, String configName)
    {
        this(domain);
        this.configPath = configName + ".cfg";
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, getProxy());
        if (this.getClass().toString().contains("com.builtbroken"))
        {
            if (configPath == null || configPath.isEmpty())
            {
                config = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/" + event.getSuggestedConfigurationFile().getName()));
            }
            else
            {
                config = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/" + configPath));
            }
        }
        else
        {
            if (configPath == null || configPath.isEmpty())
            {
                config = new Configuration(event.getSuggestedConfigurationFile());
            }
            else
            {
                config = new Configuration(new File(event.getModConfigurationDirectory(), configPath));
            }
        }
        getConfig().load();
        loader.applyModule(getProxy());
        if (fireProxyPreInit)
        {
            loader.preInit();
        }
    }

    public void init(FMLInitializationEvent event)
    {
        loader.init();
        getManager().fireInit();
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        loader.postInit();
        getManager().firePostInit();
        getConfig().save();
    }

    public Configuration getConfig()
    {
        return config;
    }

    public ModManager getManager()
    {
        return this.manager;
    }

    public Logger logger()
    {
        return this.logger;
    }

    public abstract AbstractProxy getProxy();
}
