package com.builtbroken.mc.framework.json.imp;

import net.minecraft.item.ItemStack;

/**
 * Used to provide additional data about internal states of a JSON generated object
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/10/2017.
 */
public interface IJsonKeyDataProvider
{
    /**
     * Called to get data about an internal state
     * <p>
     * Examples:
     * primaryColor = Integer color
     * cookTime = int
     * customName = string
     *
     * @param key - key
     * @param -   item being used for some process that needs the data
     * @return data about the key
     */
    default Object getJsonKeyData(String key, ItemStack itemStack)
    {
        return null;
    }

    /**
     * Called to get data about an internal state
     * <p>
     * Examples:
     * primaryColor = Integer color
     * cookTime = int
     * customName = string
     *
     * @param key - key
     * @return data about the key
     */
    default Object getJsonKeyData(String key)
    {
        return null;
    }
}
