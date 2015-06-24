package com.builtbroken.mc.api.items;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Dark on 6/23/2015.
 */
public interface ICastItem
{
    /**
     * Allows the cast to restrict the fluid that can enter it, if this returns fall it calls
     * isDestroyed
     *
     * @param stack - this item
     * @param fluid - fluid
     * @return true if allow, by default it is suggested to return true unless the cast is made of ice or something
     */
    boolean allowFluid(ItemStack stack, Fluid fluid);

    /**
     * Sets the capacity of a FluidTank that acts as the cast's fluid buffer cast
     *
     * @param stack - this item
     * @return volume, 1000 = one bucket, 144 = one ingot
     */
    int getFluidCapacity(ItemStack stack);

    /**
     * Type of cast
     * @param stack - this item
     * @return valid type registered with Molten cast handler
     */
    String getCastType(ItemStack stack);

    /**
     * Called when the cast is completely filled and the cast timer finishes
     *
     * @param stack      - this item
     * @param fluidStack - fluid filling the cast
     * @param cast       - Tile that contains the cast
     * @return The item that is created from the cast
     */
    ItemStack doCast(ItemStack stack, FluidStack fluidStack, TileEntity cast);

    /**
     * Called to check if the cast is destroyed when a fluid is poored into it
     * @param stack - this item
     * @param fluid - fluid object
     * @param amount - volume added
     * @param volume - new tank volume
     * @return new ItemStack to take the cast's place, or null to destroy the stack
     */
    ItemStack onFluidAdded(ItemStack stack, Fluid fluid, int amount, int volume);
}
