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
    private int slot = -1;
    private int nextSlot = 0;
    private boolean nonNullSlots;

    public InventoryIterator(IInventory inventory, boolean nonNullSlots)
    {
        this.inventory = inventory;
        this.nonNullSlots = nonNullSlots;
    }

    /**
     * Current index/slot in the inventory
     *
     * @return slot number
     */
    public int slot()
    {
        return slot;
    }

    /**
     * Next slot in the search, this is normally just
     * index + 1 however if {@link #nonNullSlots} is
     * true this will be the next non-null value
     * in the inventory.
     *
     * @return next slot index number
     */
    public int nextSlot()
    {
        return nextSlot;
    }

    @Override
    public boolean hasNext()
    {
        //Ensure the slot index moves forward automatically
        if (nonNullSlots && inventory.getStackInSlot(nextSlot()) == null)
        {
            while (nextSlot() < inventory.getSizeInventory() && inventory.getStackInSlot(nextSlot()) == null)
            {
                nextSlot += 1;
            }
        }
        return nextSlot() < inventory.getSizeInventory() && (!nonNullSlots || inventory.getStackInSlot(nextSlot()) != null);
    }

    @Override
    public ItemStack next()
    {
        if (slot() >= inventory.getSizeInventory())
        {
            return null;
        }
        slot = nextSlot();
        nextSlot += 1;
        return inventory.getStackInSlot(slot());
    }

    @Override
    public void remove()
    {
        inventory.setInventorySlotContents(slot(), null);
    }

    //TODO implement set slot from iterator
    //TODO implement decrease slot size

    @Override
    public Iterator<ItemStack> iterator()
    {
        return this;
    }
}
