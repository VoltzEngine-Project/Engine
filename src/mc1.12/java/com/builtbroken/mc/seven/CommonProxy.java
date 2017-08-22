package com.builtbroken.mc.seven;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.registry.CommonRegistryProxy;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Shared loading functionality
 */
public class CommonProxy extends AbstractProxy
{
    @Deprecated
    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    public void onLoad()
    {
        ModManager.proxy = new CommonRegistryProxy();
        Engine.minecraft = MinecraftWrapper.INSTANCE = new MinecraftWrapper();
    }

    @Override
    public void init()
    {
        //EntityRegistry.registerGlobalEntityID(EntityExCreeper.class, "ExCreeper", EntityRegistry.findGlobalUniqueEntityId());

        //TODO move to JSON
        EntityRegistry.registerModEntity(EntityExCreeper.class, "ExCreeper", 55, Engine.loaderInstance, 100, 1, true);
    }
}
