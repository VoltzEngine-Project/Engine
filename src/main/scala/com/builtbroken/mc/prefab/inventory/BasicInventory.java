package com.builtbroken.mc.prefab.inventory;

import com.builtbroken.mc.api.ISave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by robert on 5/1/2015.
 */
public class BasicInventory implements ISave, IInventory
{
    /**
     * Default slot max count
     */
    protected int slots;

    /**
     * Items contained in this inv
     */
    protected ItemStack[] containedItems;

    public BasicInventory(int slots)
    {
        this.slots = slots;
    }

    @Override
    public int getSizeInventory()
    {
        return slots;
    }

    public ItemStack[] getContainedItems()
    {
        if (this.containedItems == null)
        {
            this.containedItems = new ItemStack[this.getSizeInventory()];
        }
        return this.containedItems;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot < getContainedItems().length)
        {
            return this.getContainedItems()[slot];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int ammount)
    {
        if (this.getContainedItems()[slot] != null)
        {
            ItemStack var3;

            if (this.getContainedItems()[slot].stackSize <= ammount)
            {
                var3 = this.getContainedItems()[slot];
                getContainedItems()[slot] = null;
                markDirty();
                return var3;
            }
            else
            {
                var3 = this.getContainedItems()[slot].splitStack(ammount);

                if (this.getContainedItems()[slot].stackSize == 0)
                {
                    this.getContainedItems()[slot] = null;
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
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.getContainedItems()[par1] != null)
        {
            ItemStack var2 = this.getContainedItems()[par1];
            this.getContainedItems()[par1] = null;
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
        ItemStack pre_stack = getStackInSlot(slot) != null ? getStackInSlot(slot).copy() : null;
        this.getContainedItems()[slot] = insertStack;

        if (!InventoryUtility.stacksMatchExact(pre_stack, getStackInSlot(slot)))
        {
            markDirty();
        }
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
        return i >= this.getSizeInventory();
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
        this.containedItems = null;

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

        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
            if (this.getStackInSlot(i) != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(var4);
                nbtList.appendTag(var4);
            }
        }

        nbt.setTag("Items", nbtList);
        return nbt;
    }
}
