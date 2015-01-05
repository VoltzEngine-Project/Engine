package com.builtbroken.mc.lib.mod.content;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;

public class CommonRegistryProxy
{
    public void registerTileEntity(String name, String prefix, Class<? extends TileEntity> clazz)
    {
        GameRegistry.registerTileEntityWithAlternatives(clazz, prefix + name, name);
    }

    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {

    }
}
