package resonant.lib.content.module.prefab;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IExternalInventory;
import resonant.api.IExternalInventoryBox;
import resonant.lib.content.module.TileBase;
import resonant.lib.utility.inventory.ExternalInventory;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.api.vector.Vector3;

/** Prefab for tiles that need a basic inventory
 * 
 * @author Darkguardsman */
public class TileInventory extends TileBase implements IExternalInventory, ISidedInventory
{
    public TileInventory(String name, Material material)
    {
        super(name, material);
    }

    public TileInventory(Material material)
    {
        super(material);
    }

    protected IExternalInventoryBox inventory;
    protected int maxSlots = 1;

    @Override
    public IExternalInventoryBox getInventory()
    {
        if (inventory == null)
        {
            inventory = new ExternalInventory(this, this.maxSlots);
        }

        return inventory;
    }

    /** Gets the container class that goes with this tileEntity when creating a gui */
    public Class<? extends Container> getContainer()
    {
        return null;
    }

    @Override
    public int getSizeInventory()
    {
        return this.getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return this.getInventory().getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return this.getInventory().decrStackSize(i, j);
    }

    public void incrStackSize(int slot, ItemStack itemStack)
    {
        if (this.getStackInSlot(slot) == null)
        {
            setInventorySlotContents(slot, itemStack.copy());
        }
        else if (this.getStackInSlot(slot).isItemEqual(itemStack))
        {
            getStackInSlot(slot).stackSize += itemStack.stackSize;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return this.getInventory().getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        this.getInventory().setInventorySlotContents(i, itemStack);
    }

    @Override
    public String getInvName()
    {
        return getBlockType().getLocalizedName();
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return this.getInventory().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.getInventory().isUseableByPlayer(entityplayer);
    }

    @Override
    public void openChest()
    {
        this.getInventory().openChest();

    }

    @Override
    public void closeChest()
    {
        this.getInventory().closeChest();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return this.getInventory().isItemValidForSlot(i, itemstack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return this.getInventory().getAccessibleSlotsFromSide(var1);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        return this.getInventory().canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return this.getInventory().canExtractItem(i, itemstack, j);
    }

    @Override
    public boolean canStore(ItemStack stack, int slot, ForgeDirection side)
    {
        return false;
    }

    @Override
    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side)
    {
        if (slot >= this.getSizeInventory())
        {
            return false;
        }

        return true;
    }

    /** Player-Inventory interaction methods. */
    public boolean interactCurrentItem(int slotID, EntityPlayer player)
    {
        return interactCurrentItem(this, slotID, player);
    }

    public boolean interactCurrentItem(IInventory inventory, int slotID, EntityPlayer player)
    {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);
        ItemStack current = player.inventory.getCurrentItem();

        /** Try to insert. */
        if (current != null)
        {
            if (stackInInventory == null || ItemStack.areItemStacksEqual(stackInInventory, current))
            {
                return insertCurrentItem(inventory, slotID, player);
            }
        }

        /** Try to extract. */
        return extractItem(inventory, slotID, player);
    }

    public boolean insertCurrentItem(IInventory inventory, int slotID, EntityPlayer player)
    {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);
        ItemStack current = player.inventory.getCurrentItem();

        if (current != null)
        {
            if (stackInInventory == null || ItemStack.areItemStacksEqual(stackInInventory, current))
            {
                if (inventory.isItemValidForSlot(slotID, current))
                {
                    /** If control is down, insert one only. */
                    if (isControlDown(player))
                    {
                        if (stackInInventory == null)
                        {
                            inventory.setInventorySlotContents(slotID, current.splitStack(1));
                        }
                        else
                        {
                            stackInInventory.stackSize++;
                            current.stackSize--;
                        }
                    }
                    else
                    {
                        if (stackInInventory == null)
                        {
                            inventory.setInventorySlotContents(slotID, current);
                        }
                        else
                        {
                            stackInInventory.stackSize += current.stackSize;
                            current.stackSize = 0;
                        }

                        current = null;
                    }

                    if (current == null || current.stackSize <= 0)
                    {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public boolean extractItem(IInventory inventory, int slotID, EntityPlayer player)
    {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);

        if (stackInInventory != null)
        {
            /** If control is down, insert one only. */
            if (isControlDown(player))
            {
                InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory.splitStack(1), 0);
            }
            else
            {
                InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory, 0);
                stackInInventory = null;
            }

            if (stackInInventory == null || stackInInventory.stackSize <= 0)
            {
                inventory.setInventorySlotContents(slotID, null);
            }

            return true;
        }

        return false;
    }

    @Override
    public void onRemove(int par5, int par6)
    {
        super.onRemove(par5, par6);
        dropEntireInventory(par5, par6);
    }

    public void dropEntireInventory(int par5, int par6)
    {
        if (this instanceof IInventory)
        {
            IInventory inventory = this;

            for (int i = 0; i < inventory.getSizeInventory(); ++i)
            {
                ItemStack dropStack = inventory.getStackInSlot(i);

                if (dropStack != null)
                {
                    int var11 = dropStack.stackSize;
                    dropStack.stackSize -= var11;
                    InventoryUtility.dropItemStack(world(), center(), dropStack);

                    if (dropStack.stackSize <= 0)
                        inventory.setInventorySlotContents(i, null);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getInventory().load(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        getInventory().save(nbt);
    }
}
