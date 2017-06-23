package com.builtbroken.mc.core.registry;

import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;
import java.util.Map;

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
        try
        {
            GameRegistry.registerTileEntity(tile.getClass(), prefix + name);
        }
        catch (IllegalArgumentException exception)
        {
            if (exception.getMessage().contains("Duplicate id:"))
            {
                try
                {
                    Field field = TileEntity.class.getDeclaredField("nameToClassMap");
                    field.setAccessible(true);
                    Map map = (Map) field.get(null);
                    if (map.containsKey(name))
                    {
                        Class c = (Class) map.get(name);
                        if (c != tile.getClass())
                        {
                            throw new RuntimeException("IDs matched but classes for entities did not. This is either a code error or conflict between two mods.", exception);
                        }
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Failed to check for duplicate tile IDs matching", e);
                }
            }
            else
            {
                throw exception;
            }
        }
    }

    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {

    }

    public void onRegistry(Object object)
    {
        if (object instanceof IRegistryInit)
        {
            ((IRegistryInit) object).onRegistered();
        }
    }
}
