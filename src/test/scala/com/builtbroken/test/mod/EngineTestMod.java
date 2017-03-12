package com.builtbroken.test.mod;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.mod.AbstractMod;
import com.builtbroken.mc.lib.mod.AbstractProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/12/2017.
 */
@Mod(modid = EngineTestMod.DOMAIN, name = "Voltz Engine Test Mod", version = References.VERSION, acceptableRemoteVersions = "*", dependencies = "required-after:" + References.ID)
public class EngineTestMod extends AbstractMod
{
    public static final String DOMAIN = "voltzenginetest";

    @SidedProxy(clientSide = "com.builtbroken.test.mod.ClientProxy", serverSide = "com.builtbroken.test.mod.CommonProxy")
    public static CommonProxy proxy;


    @Mod.Instance(DOMAIN)
    public static EngineTestMod instance;

    public EngineTestMod()
    {
        super(DOMAIN);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        //Keep in mind some content is loaded from the json system
        //      This mod class exists to force the content loader
        //      to run on the domain.
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        super.loadComplete(event);
    }

    @Override
    public AbstractProxy getProxy()
    {
        return proxy;
    }
}
