package com.builtbroken.lib.mod.content;

import com.builtbroken.mod.BBL;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;
import com.builtbroken.mod.References;
import com.builtbroken.lib.utility.ReflectionUtility;

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
