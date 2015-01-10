package com.builtbroken.mc.api.recipe;

import net.minecraft.inventory.IInventory;

/**
 * Handles the crafting for machine recipes to avoid common issues
 * for example bucket handling.
 *
 * Created by robert on 1/9/2015.
 */
public interface IMachineCraftingHandler
{
    /**
     * Called to handle the actual crafting for the machine
     *
     * @param inv          - inventory to access slots from
     * @param inputs       - inputs used to make the recipe
     * @param outputs      - output from the recipe
     * @param inputSlots   - inventory slots that can be inputted to
     * @param outputSlots  - inventory slots that can be outputted to
     * @param storageSlots - optional, used for more complex tiles
     */
    public void doCrafting(IInventory inv, Object inputs, Object outputs, int[] inputSlots, int[] outputSlots, int[] storageSlots);

}
