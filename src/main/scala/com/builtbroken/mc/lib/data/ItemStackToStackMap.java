package com.builtbroken.mc.lib.data;

import com.builtbroken.mc.core.Engine;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class ItemStackToStackMap extends HashMapNotNull<ItemStack, ItemStack>
{
    public ItemStackToStackMap(String name)
    {
        super(name);
    }

    public ItemStackToStackMap(String name, Map<? extends ItemStack, ? extends ItemStack> map)
    {
        super(name, map);
    }

    @Override
    public ItemStack put(ItemStack key, ItemStack value)
    {
        if (key != null && key.getItem() != null && value != null && value.getItem() != null)
        {
            return super.put(key, value);
        }
        else
        {
            ModContainer container = Loader.instance().activeModContainer();
            Engine.logger().error("ItemStackToStackMap: " + (container != null ? container.getName() : "Something") + " tried to insert an invalid value of [K: " + key + " V: " + value + "] into map '" + name + "'", new RuntimeException());
        }
        return null;
    }
}
