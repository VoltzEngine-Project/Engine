package resonant.lib.utility.inventory;

import resonant.api.IExternalInventory;
import resonant.api.IExternalInventoryBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class ExternalInventory implements IExternalInventoryBox
{
    /** Access able slots side all */
    protected int[] openSlots;
    /** Items contained in this inv */
    protected ItemStack[] containedItems;
    /** Host tileEntity */
    protected TileEntity hostTile;
    /** Host tileEntity as external inv */
    protected IExternalInventory inv;
    /** Default slot max count */
    protected final int slots;

    public ExternalInventory(TileEntity chest, IExternalInventory inv, int slots)
    {
        this.hostTile = chest;
        this.slots = slots;
        this.inv = inv;
    }

    public ExternalInventory(TileEntity chest, int slots)
    {
        this(chest, ((IExternalInventory) chest), slots);
    }

    public ExternalInventory(Entity entity, int i)
    {
        this.slots = i;
        this.inv = (IExternalInventory) entity;
    }

    @Override
    public int getSizeInventory()
    {
        return slots;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot < getContainedItems().length)
            return this.getContainedItems()[slot];
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
                this.getContainedItems()[slot] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.getContainedItems()[slot].splitStack(ammount);

                if (this.getContainedItems()[slot].stackSize == 0)
                {
                    this.getContainedItems()[slot] = null;
                }

                this.onInventoryChanged();
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
        ItemStack pre_stack = this.getContainedItems()[slot] != null ? this.getContainedItems()[slot].copy() : null;
        this.getContainedItems()[slot] = insertStack;

        if (insertStack != null && insertStack.stackSize > this.getInventoryStackLimit())
        {
            insertStack.stackSize = this.getInventoryStackLimit();
        }
        if (!InventoryUtility.stacksMatchExact(pre_stack, getContainedItems()[slot]))
        {
            //System.out.println("External inv slot change " + pre_stack + " to " + getContainedItems()[slot]);
            this.onInventoryChanged();
        }
    }

    @Override
    public String getInvName()
    {
        return "container.chest";
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        if (i >= this.getSizeInventory())
        {
            return false;
        }
        return true;
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
        return this.isItemValidForSlot(i, itemstack) && this.inv.canStore(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return this.inv.canRemove(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {
        if (this.hostTile != null)
        {
            this.hostTile.onInventoryChanged();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        if (this.hostTile != null)
        {
            return this.hostTile.worldObj.getBlockTileEntity(this.hostTile.xCoord, this.hostTile.yCoord, this.hostTile.zCoord) != this.hostTile ? false : par1EntityPlayer.getDistanceSq(this.hostTile.xCoord + 0.5D, this.hostTile.yCoord + 0.5D, this.hostTile.zCoord + 0.5D) <= 64.0D;
        }
        return true;
    }

    @Override
    public ItemStack[] getContainedItems()
    {
        if (this.containedItems == null)
        {
            this.containedItems = new ItemStack[this.getSizeInventory()];
        }
        return this.containedItems;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.clear();

        NBTTagList nbtList = nbt.getTagList("Items");

        for (int i = 0; i < nbtList.tagCount(); ++i)
        {
            NBTTagCompound stackTag = (NBTTagCompound) nbtList.tagAt(i);
            byte id = stackTag.getByte("Slot");

            if (id >= 0 && id < this.getSizeInventory())
            {
                this.setInventorySlotContents(id, ItemStack.loadItemStackFromNBT(stackTag));
            }
        }

        nbt.setTag("Items", nbtList);
    }

    @Override
    public void save(NBTTagCompound nbt)
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
    }

    @Override
    public void clear()
    {
        this.containedItems = null;
        this.getContainedItems();
    }

}
