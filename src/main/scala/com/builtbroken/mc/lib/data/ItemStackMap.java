package com.builtbroken.mc.lib.data;

import com.builtbroken.mc.core.Engine;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class ItemStackMap<V> extends HashMapNotNull<ItemStack, V>
{
    public ItemStackMap(String name)
    {
        super(name);
    }

    public ItemStackMap(String name, Map<? extends ItemStack, ? extends V> map)
    {
        super(name, map);
    }

    @Override
    public V put(ItemStack key, V value)
    {
        if (key != null && key.getItem() != null && value != null)
        {
            return super.put(key, value);
        }
        else
        {
            Engine.logger().error("ItemStackMap: Something tried to insert an invalid value of [K: " + key + " V: " + value + "] into map '" + name + "'", new RuntimeException());
        }
        return null;
    }
}
