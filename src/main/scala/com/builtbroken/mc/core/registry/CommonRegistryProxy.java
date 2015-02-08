package com.builtbroken.mc.core.registry;

import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

public class CommonRegistryProxy
{
    public void registerBlock(ModManager manager, String name, String prefix, Block block, Class<? extends ItemBlock> itemBlock)
    {
        GameRegistry.registerBlock(block, itemBlock != null ? itemBlock : ItemBlock.class, name);
    }

    public void registerItem(ModManager manager, String name, String modPrefix, Item item)
    {
        GameRegistry.registerItem(item, name);
    }

    public void registerTileEntity(String name, String prefix, Block block, TileEntity tile)
    {
        GameRegistry.registerTileEntityWithAlternatives(tile.getClass(), prefix + name, name, tile.getClass().getSimpleName(), prefix + tile.getClass().getSimpleName());
    }

    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {

    }

    public void onRegistry(Object object)
    {
        if(object instanceof IRegistryInit)
        {
            ((IRegistryInit) object).onRegistered();
        }
    }
}
