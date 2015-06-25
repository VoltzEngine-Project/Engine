package com.builtbroken.mc.api.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Dark on 6/23/2015.
 */
public interface I2DCastItem
{
    /**
     * IIcon that will be extruded into the cast model
     * @param stack - this item
     * @return
     */
    @SideOnly(Side.CLIENT)
    IIcon getCastIcon(ItemStack stack);
}
