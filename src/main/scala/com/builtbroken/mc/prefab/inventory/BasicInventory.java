package com.builtbroken.mc.prefab.inventory;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.core.Engine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

/**
 * Simple inventory implementation
 * Created by robert on 5/1/2015.
 */
public class BasicInventory implements ISave, IInventory, Iterable<Map.Entry<Integer, ItemStack>>
{
    /** Default slot max count */
    protected int slots;

    /**
     * How much to shift the start of the inventory map,
     * used to adjust save/load process accordingly
     */
    protected int shiftSlotStart = 0;

    protected boolean recalculateFillStatus = true;
    protected boolean isFull = false;

    /** Map of the inventory */
    protected HashMap<Integer, ItemStack> inventoryMap = new HashMap();

    public BasicInventory(int slots)
    {
        this.slots = slots;
    }

    @Override
    public int getSizeInventory()
    {
        return slots;
    }

    public Collection<ItemStack> getContainedItems()
    {
        return this.inventoryMap.values();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot >= 0 && slot < getSizeInventory())
        {
            return this.inventoryMap.containsKey(slot) ? this.inventoryMap.get(slot) : null;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int ammount)
    {
        if (this.getStackInSlot(slot) != null)
        {
            ItemStack var3;

            if (this.getStackInSlot(slot).stackSize <= ammount)
            {
                var3 = this.getStackInSlot(slot);
                setInventorySlotContents(slot, null);
                markDirty();
                return var3;
            }
            else
            {
                var3 = this.getStackInSlot(slot).splitStack(ammount);

                if (this.getStackInSlot(slot).stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }

                markDirty();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (this.getStackInSlot(slot) != null)
        {
            ItemStack var2 = this.getStackInSlot(slot);
            this.inventoryMap.remove(slot);
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack insertStack)
    {
        if (slot >= 0 && slot < getSizeInventory())
        {
            ItemStack pre_stack = getStackInSlot(slot) != null ? getStackInSlot(slot).copy() : null;
            if (insertStack != null)
            {
                inventoryMap.put(slot, insertStack);
            }
            else if (inventoryMap.containsKey(slot))
            {
                inventoryMap.remove(slot);
            }
            if (!InventoryUtility.stacksMatchExact(pre_stack, getStackInSlot(slot)))
            {
                recalculateFillStatus = true;
                onInventoryChanged(slot, pre_stack, getStackInSlot(slot));
            }
        }
        else
        {
            Engine.error("BasicInventory: something tried to set " + insertStack + " into slot " + slot + " which is outside the 0 - " + (getSizeInventory() - 1) + " limit");
        }
    }

    /**
     * Called when the stack in the inventory slot has changed
     *
     * @param slot
     * @param prev
     * @param item
     */
    protected void onInventoryChanged(int slot, ItemStack prev, ItemStack item)
    {
        markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return "container.inventory.basic";
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return i >= this.getSizeInventory() && i < getSizeInventory();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.inventoryMap.clear();

        NBTTagList nbtList = nbt.getTagList("Items", 10);

        for (int i = 0; i < nbtList.tagCount(); ++i)
        {
            NBTTagCompound stackTag = nbtList.getCompoundTagAt(i);
            byte id = stackTag.getByte("Slot");

            if (id >= 0 && id < this.getSizeInventory())
            {
                this.setInventorySlotContents(id, ItemStack.loadItemStackFromNBT(stackTag));
            }
        }

        nbt.setTag("Items", nbtList);
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        NBTTagList nbtList = new NBTTagList();

        for (int i = shiftSlotStart; i < this.getSizeInventory() + shiftSlotStart; ++i)
        {
            if (this.getStackInSlot(i + shiftSlotStart) != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) i);
                this.getStackInSlot(i + shiftSlotStart).writeToNBT(var4);
                nbtList.appendTag(var4);
            }
        }

        nbt.setTag("Items", nbtList);
        return nbt;
    }

    /**
     * Called to see if the inventory is empty
     *
     * @return true if nothing is contained
     */
    public boolean isEmpty()
    {
        return inventoryMap.isEmpty();
    }

    public boolean isFull()
    {
        if (recalculateFillStatus)
        {
            recalculateFillStatus = false;

            for (int i = 0; i < getSizeInventory(); i++)
            {
                if (InventoryUtility.roomLeftInSlot(this, i) > 0)
                {
                    isFull = false;
                    return false;
                }
            }
            isFull = true;
        }
        return isFull;
    }

    public ArrayList<Integer> getFilledSlots()
    {
        return InventoryUtility.getFilledSlots(this);
    }

    public ArrayList<Integer> getEmptySlots()
    {
        return InventoryUtility.getEmptySlots(this);
    }

    public ArrayList<Integer> getSlotsWithSpace()
    {
        return InventoryUtility.getSlotsWithSpace(this);
    }

    @Override
    public Iterator<Map.Entry<Integer, ItemStack>> iterator()
    {
        return inventoryMap.entrySet().iterator();
    }

    @Override
    public String toString()
    {
        return "BasicInventory[" + getInventoryName() + ", " + getSizeInventory() + "]@" + hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof BasicInventory)
        {
            return ((BasicInventory) object).slots == slots && ((BasicInventory) object).inventoryMap == inventoryMap;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return inventoryMap != null ? inventoryMap.hashCode() : super.hashCode();
    }
}
