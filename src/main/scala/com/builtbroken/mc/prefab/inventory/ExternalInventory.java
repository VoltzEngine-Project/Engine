package com.builtbroken.mc.prefab.inventory;

import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.api.tile.node.IExternalInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Version of the basic ISidedInventory that is designed to be used as a replacement for
 * the conventional inventory used in machines.
 *
 * @author Darkguardsman
 */
public class ExternalInventory extends BasicInventory implements IExternalInventory, ISidedInventory
{
    /**
     * Access able slots side all
     */
    protected int[] openSlots;
    /**
     * Host tileEntity
     */
    protected IInventoryProvider host;

    public ExternalInventory(IInventoryProvider inv, int slots)
    {
        super(slots);
        this.host = inv;
    }

    protected TileEntity tile()
    {
        return (TileEntity) host;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return i < this.getSizeInventory();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        if (openSlots == null || openSlots.length != this.getSizeInventory())
        {
            this.openSlots = new int[this.getSizeInventory()];
            for (int i = 0; i < this.openSlots.length; i++)
            {
                openSlots[i] = i;
            }
        }
        return this.openSlots;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        return this.isItemValidForSlot(i, itemstack) && host.canStore(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return host.canRemove(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
        if (host instanceof TileEntity)
        {
            tile().markDirty();
        }
    }

    @Override
    protected void onInventoryChanged(int slot, ItemStack prev, ItemStack item)
    {
        host.onInventoryChanged(slot, prev, item);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void clear()
    {
        this.inventoryMap.clear();
    }
}
