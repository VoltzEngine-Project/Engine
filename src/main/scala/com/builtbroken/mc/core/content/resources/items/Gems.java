package com.builtbroken.mc.core.content.resources.items;

import com.builtbroken.mc.lib.helper.LanguageUtility;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.config.Configuration;

/**
 * @author - Kolatra
 */
public enum Gems
{
    Amazonite(),
    SmokeyQuartz(),
    OnyxBlack(),
    OnyxRed(),
    GarnetGreen(),
    GarnetOrange(),
    GarnetRed(),
    GarnetYellow();

    private Item item;
    private String oreDict;

    Gems()
    {

    }

    public static void registerSet(Item item, Configuration config)
    {
        for (Gems gems : Gems.values())
        {
            gems.item = item;
            gems.oreDict = "gem" + LanguageUtility.capitalizeFirst(gems.name().toLowerCase());
            ItemStack stack = gems.stack(1);
            GameRegistry.registerCustomItemStack(gems.oreDict, stack);
        }
    }

    public ItemStack stack(int stackSize)
    {
        return new ItemStack(item, stackSize, ordinal());
    }
}
