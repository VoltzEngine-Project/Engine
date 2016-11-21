package com.builtbroken.mc.prefab.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

/**
 * Wrapper for inventory to allow iteration easier
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/21/2016.
 */
public class InventoryIterator implements Iterator<ItemStack>, Iterable<ItemStack>
{
    private final IInventory inventory;
    private int slot;
    private boolean nonNullSlots;

    public InventoryIterator(IInventory inventory, boolean nonNullSlots)
    {
        this.inventory = inventory;
        this.nonNullSlots = nonNullSlots;
    }

    public int slot()
    {
        return slot;
    }

    @Override
    public boolean hasNext()
    {
        //Ensure the slot index moves forward automatically
        if (nonNullSlots && inventory.getStackInSlot(slot) == null)
        {
            do
            {
                slot++;
            }
            while (slot < inventory.getSizeInventory() && inventory.getStackInSlot(slot) != null);
        }
        return slot() < inventory.getSizeInventory();
    }

    @Override
    public ItemStack next()
    {
        if (slot() >= inventory.getSizeInventory())
        {
            return null;
        }
        return inventory.getStackInSlot(slot++);
    }

    @Override
    public Iterator<ItemStack> iterator()
    {
        return this;
    }
}
