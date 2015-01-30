package com.builtbroken.mc.core.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class CommonRegistryProxy
{
    public void registerTileEntity(String name, String prefix, Block block, TileEntity tile)
    {
        GameRegistry.registerTileEntityWithAlternatives(tile.getClass(), prefix + name, name, tile.getClass().getSimpleName(), prefix + tile.getClass().getSimpleName());
    }

    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {

    }
}
