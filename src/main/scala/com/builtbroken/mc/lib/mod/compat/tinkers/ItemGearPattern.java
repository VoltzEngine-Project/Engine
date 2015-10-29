package com.builtbroken.mc.lib.mod.compat.tinkers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.util.IPattern;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public class ItemGearPattern extends Item implements IPattern
{
    @Override
    public int getPatternCost(ItemStack itemStack)
    {
        return 0;
    }

    @Override
    public ItemStack getPatternOutput(ItemStack itemStack, ItemStack itemStack1, PatternBuilder.MaterialSet materialSet)
    {
        return null;
    }
}
