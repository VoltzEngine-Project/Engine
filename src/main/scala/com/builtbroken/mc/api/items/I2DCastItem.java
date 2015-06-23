package com.builtbroken.mc.api.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Dark on 6/23/2015.
 */
@SideOnly(Side.CLIENT)
public interface I2DCastItem
{
    /**
     * IIcon that will be extruded into the cast model
     * @param stack - this item
     * @return
     */
    IIcon getCastIcon(ItemStack stack);

    /**
     * The icon used to render the item being casted
     * @param stack - this item
     * @param fluid - fluid being casted
     * @return
     */
    IIcon getCastedItemIcon(ItemStack stack, Fluid fluid);
}
