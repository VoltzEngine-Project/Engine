package com.builtbroken.mc.prefab.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Java 8 default implementation of the inventory system
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2016.
 */
public interface IPrefabInventory extends IInventory
{
    //\0/ bye bye scala traits - Dark

    /** Map of all items stored by slot id */
    Map<Integer, ItemStack> getInventoryMap();

    @Override
    default ItemStack getStackInSlot(int slot)
    {
        if (getInventoryMap() != null && getInventoryMap().containsKey(slot))
        {
            return getInventoryMap().get(slot);
        }
        return null;
    }

    @Override
    default ItemStack decrStackSize(int slot, int ammount)
    {
        if (this.getStackInSlot(slot) != null)
        {
            ItemStack var3 = null;
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
    default ItemStack getStackInSlotOnClosing(int slot)
    {
        if (this.getStackInSlot(slot) != null)
        {
            ItemStack var2 = this.getStackInSlot(slot);
            setInventorySlotContents(slot, null);
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    default void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {

    }

    @Override
    default String getInventoryName()
    {
        return "inventory." + getClass().getName();
    }

    @Override
    default boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    default int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    default void markDirty()
    {

    }

    @Override
    default boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return true;
    }

    @Override
    default void openInventory()
    {
    }

    @Override
    default void closeInventory()
    {
    }

    @Override
    default boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return true;
    }
}
