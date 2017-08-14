package com.builtbroken.mc.core;

import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.registry.CommonRegistryProxy;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

/**
 * The Voltz Engine common proxy
 *
 * @author Calclavia
 */
public class CommonProxy extends AbstractProxy
{
    @Deprecated
    public static CommonProxy proxy;

    //config files
    @Deprecated //Will be replaced by an encapsulated system later
    public static Configuration heatDataConfig;
    @Deprecated //Will be replaced by an encapsulated system later
    public static Configuration explosiveConfig;

    @Deprecated //Temp until rewrite is finished
    public static Configuration configuration;

    @Deprecated
    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    public void loadModManager()
    {
        ModManager.proxy = new CommonRegistryProxy();
    }

    @Override
    public void init()
    {
        //EntityRegistry.registerGlobalEntityID(EntityExCreeper.class, "ExCreeper", EntityRegistry.findGlobalUniqueEntityId());

        //TODO move to JSON
        EntityRegistry.registerModEntity(EntityExCreeper.class, "ExCreeper", 55, Engine.loaderInstance, 100, 1, true);
    }
}
