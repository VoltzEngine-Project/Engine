package com.builtbroken.api.items;

import net.minecraft.item.ItemStack;

/**
 * Created by robert on 1/4/2015.
 */
public interface IEnergyBufferItem
{

    /**
     * Get the amount of energy currently stored in the item.
     */
    public double getEnergy(ItemStack theItem);

    /**
     * Get the max amount of energy that can be stored in the item.
     */
    public double getEnergyCapacity(ItemStack theItem);

    /**
     * Sets the amount of energy in the ItemStack. Use recharge or discharge instead of calling this
     * to be safer!
     *
     * @param itemStack - the ItemStack.
     * @param energy    - Amount of electrical energy.
     */
    public void setEnergy(ItemStack itemStack, double energy);
}
