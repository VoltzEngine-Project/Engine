package com.builtbroken.mc.prefab.tile;

import cofh.api.energy.IEnergyConnection;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.IUpdate;
import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.api.tile.ISided;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import com.builtbroken.mc.prefab.tile.module.TileModuleInventory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 * This is only used for Java. For Scala classes, use traits instead.
 * <p/>
 *
 * @author Darkguardsman
 */
public class TileModuleMachine extends TileMachine implements ITileModuleProvider, IInventoryProvider, ISidedInventory, IEnergyConnection
{
    protected List<ITileModule> modules = new ArrayList();
    protected TileModuleInventory inventory_module = null;

    public TileModuleMachine(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public void onNeighborChanged(Block block)
    {
        super.onNeighborChanged(block);
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onParentChange();
            }
        }
    }

    @Override
    public void onWorldJoin()
    {
        super.onWorldJoin();
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onJoinWorld();
            }
        }
    }

    @Override
    public void update()
    {
        super.update();
        for (ITileModule node : getNodes())
        {
            if (node instanceof IUpdate)
            {
                ((IUpdate) node).update();
            }
        }
    }

    @Override
    public void invalidate()
    {
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onLeaveWorld();
            }
        }
        super.invalidate();
    }

    /**
     * Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc
     */
    protected final List<ITileModule> getNodes()
    {
        return modules;
    }

    @Override
    public <N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from)
    {
        for (ITileModule module : getNodes())
        {
            if (!(module instanceof ISided) || ((ISided) module).isValidForSide(from))
            {
                if (nodeType.isAssignableFrom(module.getClass()))
                {
                    return (N) module;
                }
            }
        }
        return null;
    }

    public boolean addInventoryModule(int size)
    {
        if(inventory_module == null)
        {
            inventory_module = new TileModuleInventory(this, size);
            modules.add(inventory_module);
            return true;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        for (ITileModule node : getNodes())
        {
            if (node instanceof ISave)
            {
                ((ISave) node).load(nbt);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        for (ITileModule node : getNodes())
        {
            if (node instanceof ISave)
            {
                ((ISave) node).save(nbt);
            }
        }
    }

    @Override
    public TileModuleInventory getInventory()
    {
        return inventory_module;
    }

    @Override
    public boolean canStore(ItemStack stack, int slot, ForgeDirection side)
    {
        return false;
    }

    @Override
    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side)
    {
        return false;
    }

    //==================================
    //====== Inventory redirects =======
    //==================================

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (getInventory() != null)
        {
            return getInventory().getAccessibleSlotsFromSide(side);
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side)
    {
        if (getInventory() != null)
        {
            return getInventory().canInsertItem(side, itemStack, side);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side)
    {
        if (getInventory() != null)
        {
            return getInventory().canExtractItem(side, itemStack, side);
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
        return "";
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

    //==================================
    //========== RF Support ============
    //==================================

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return false;
    }
}
