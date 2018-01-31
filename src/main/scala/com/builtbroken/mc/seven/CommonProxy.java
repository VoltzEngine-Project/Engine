package com.builtbroken.mc.seven;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Shared loading functionality
 */
public class CommonProxy extends AbstractProxy
{
    public void onLoad()
    {
        Engine.minecraft = MinecraftWrapper.INSTANCE = new MinecraftWrapper();
    }

    @Override
    public void init()
    {
        //EntityRegistry.registerGlobalEntityID(EntityExCreeper.class, "ExCreeper", EntityRegistry.findGlobalUniqueEntityId());

        //TODO move to JSON
        EntityRegistry.registerModEntity(new ResourceLocation(References.DOMAIN, "creeper"), EntityExCreeper.class, "ExCreeper", 55, Engine.loaderInstance, 100, 1, true);
    }
}
