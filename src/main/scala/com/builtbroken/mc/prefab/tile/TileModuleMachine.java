package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.api.tile.ITileConnection;
import com.builtbroken.mc.api.tile.node.ITileModule;
import com.builtbroken.mc.prefab.energy.EnergyBuffer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * An extension of {@link TileModuleMachineBase} that provides pre-implementation for common
 * interfaces that are most machines used.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/2/2017.
 */
public abstract class TileModuleMachine<I extends IInventory> extends TileModuleMachineBase implements ISidedInventory, IInventoryProvider<I>, IEnergyBufferProvider, ITileConnection
{
    /** Primary inventory container for this machine, all {@link IInventory} and {@link ISidedInventory} calls are wrapped to this object */
    protected I inventory_module;
    protected EnergyBuffer energyBuffer;

    /**
     * Default constructor
     *
     * @param name     - name of the tile, used for localizations mainly
     * @param material - material of the tile's block
     */
    public TileModuleMachine(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public I getInventory()
    {
        if (inventory_module == null)
        {
            inventory_module = createInventory();
            if (inventory_module instanceof ITileModule)
            {
                modules.add((ITileModule) inventory_module);
            }
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
        //By calling getInventory() we force the inventory to exist
        if (nbt.getBoolean("hasInventory") && getInventory() instanceof ISave)
        {
            ((ISave) getInventory()).load(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //Cache if the inventory existed so it will force load
        nbt.setBoolean("hasInventory", inventory_module != null);
        //save inventory
        if (inventory_module instanceof ISave)
        {
            ((ISave) inventory_module).save(nbt);
        }
    }

    @Override
    protected void readFromNBT(ITileModule module, NBTTagCompound nbt)
    {
        //loading is handled already for the inventory
        if (module != inventory_module)
        {
            super.readFromNBT(module, nbt);
        }
    }

    @Override
    protected void writeToNBT(ITileModule module, NBTTagCompound nbt)
    {
        //saving is handled already for the inventory
        if (module != inventory_module)
        {
            super.writeToNBT(module, nbt);
        }
    }

    //==================================
    //========== Energy code ===========
    //==================================

    @Override
    public EnergyBuffer getEnergyBuffer(ForgeDirection side)
    {
        if (energyBuffer == null && getEnergyBufferSize() > 0)
        {
            energyBuffer = new EnergyBuffer(getEnergyBufferSize());
        }
        return energyBuffer;
    }

    @Override
    public boolean canConnect(TileEntity connection, ConnectionType type, ForgeDirection from)
    {
        return getEnergyBufferSize() > 0 && type == ConnectionType.POWER;
    }

    @Override
    public boolean hasConnection(ConnectionType type, ForgeDirection side)
    {
        return false;
    }

    public int getEnergyBufferSize()
    {
        return 0;
    }

    //==================================
    //====== Inventory redirects =======
    //==================================

    public void incrStackSize(int outputSlot, ItemStack stack)
    {

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
