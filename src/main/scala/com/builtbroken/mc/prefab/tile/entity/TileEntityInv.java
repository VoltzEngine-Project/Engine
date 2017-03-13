package com.builtbroken.mc.prefab.tile.entity;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.tile.IInventoryProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Simplified impelementation for {@link ISidedInventory} and {@link IInventoryProvider}
 *
 * @param <I> - inventory type to return for {@link #getInventory()}
 */
public abstract class TileEntityInv<I extends IInventory> extends TileEntityBase implements ISidedInventory, IInventoryProvider<I>
{
    /** Primary inventory container for this machine, all {@link IInventory} and {@link ISidedInventory} calls are wrapped to this object */
    protected I inventory_module;

    @Override
    public I getInventory()
    {
        if (inventory_module == null)
        {
            inventory_module = createInventory();
        }
        return inventory_module;
    }

    /**
     * Creates a new inventory instance.
     * Called by {@link #getInventory()} if
     * {@link #inventory_module} is null
     *
     * @return new inventory
     */
    protected abstract I createInventory();

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("inventory") && getInventory() instanceof ISave)
        {
            NBTTagCompound tag = nbt.getCompoundTag("inventory");
            ((ISave)getInventory()).load(tag);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (inventory_module != null && inventory_module instanceof ISave)
        {
            NBTTagCompound tag = new NBTTagCompound();
            ((ISave)inventory_module).save(tag);
            nbt.setTag("inventory", tag);
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (getInventory() instanceof ISidedInventory)
        {
            return ((ISidedInventory) getInventory()).getAccessibleSlotsFromSide(side);
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_)
    {
        if (getInventory() instanceof ISidedInventory)
        {
            return ((ISidedInventory) getInventory()).canInsertItem(p_102007_1_, p_102007_2_, p_102007_3_);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_)
    {
        if (getInventory() instanceof ISidedInventory)
        {
            return ((ISidedInventory) getInventory()).canInsertItem(p_102008_1_, p_102008_2_, p_102008_3_);
        }
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        if (getInventory() != null)
        {
            return getInventory().getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (getInventory() != null)
        {
            return getInventory().getStackInSlot(slot);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if (getInventory() != null)
        {
            return getInventory().decrStackSize(slot, amount);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (getInventory() != null)
        {
            return getInventory().getStackInSlotOnClosing(slot);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        if (getInventory() != null)
        {
            getInventory().setInventorySlotContents(slot, stack);
        }
    }

    @Override
    public String getInventoryName()
    {
        if (getInventory() != null)
        {
            return getInventory().getInventoryName();
        }
        return "inventory";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        if (getInventory() != null)
        {
            return getInventory().hasCustomInventoryName();
        }
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (getInventory() != null)
        {
            return getInventory().getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        if (getInventory() != null)
        {
            return getInventory().isUseableByPlayer(player);
        }
        return false;
    }

    @Override
    public void openInventory()
    {
        if (getInventory() != null)
        {
            getInventory().openInventory();
        }
    }

    @Override
    public void closeInventory()
    {
        if (getInventory() != null)
        {
            getInventory().closeInventory();
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (getInventory() != null)
        {
            return getInventory().isItemValidForSlot(slot, stack);
        }
        return false;
    }
}
