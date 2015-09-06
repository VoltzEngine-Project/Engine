package com.builtbroken.mc.prefab.tile.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityInv extends TileEntity implements IInventory
{
    protected ItemStack[] chestContents = new ItemStack[36];

    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return this.chestContents[p_70301_1_];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (this.chestContents[p_70298_1_] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[p_70298_1_].stackSize <= p_70298_2_)
            {
                itemstack = this.chestContents[p_70298_1_];
                this.chestContents[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[p_70298_1_].splitStack(p_70298_2_);

                if (this.chestContents[p_70298_1_].stackSize == 0)
                {
                    this.chestContents[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        if (this.chestContents[p_70304_1_] != null)
        {
            ItemStack itemstack = this.chestContents[p_70304_1_];
            this.chestContents[p_70304_1_] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        this.chestContents[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_145841_1_.setTag("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return this.getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return true;
    }
}
