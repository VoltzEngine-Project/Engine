package com.builtbroken.mc.lib.mod;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.config.Configuration;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.mod.loadable.LoadableHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Default layout for a mod class to make it easier to keep mod.class
 * in the same general design and do the same general actions.
 *
 * You will still need to place @Mod at the top of the class, create your own proxies,
 * and do other tasks that can't be abstracted out due to @Annotations
 *
 * @Mod
 * @Instance
 * @SidedProxy
 * @EventHandler
 * @Mod.Metadata
 * @ModstatInfo
 *
 * Created by robert on 12/7/2014.
 */
public abstract class AbstractMod
{
    protected LoadableHandler loader;
    protected ModManager manager;
    protected Logger logger;
    private Configuration config;

    /**
     * @param domain - mod id uses to register textures with, etc
     */
    public AbstractMod(String domain)
    {
        loader = new LoadableHandler();
        manager = new ModManager().setPrefix(domain);
        logger = LogManager.getLogger(domain);
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, getProxy());
        config = new Configuration(event.getSuggestedConfigurationFile());
        getConfig().load();
        loader.applyModule(getProxy());
        loader.preInit();
    }

    public void init(FMLInitializationEvent event)
    {
        loader.init();
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        loader.postInit();
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

    public Logger logger() { return this.logger; }

    public abstract AbstractProxy getProxy();
}
