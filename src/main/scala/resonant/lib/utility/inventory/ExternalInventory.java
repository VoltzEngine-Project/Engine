package resonant.lib.utility.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.IExternalInventory;
import resonant.api.IInventoryProvider;

public class ExternalInventory implements IExternalInventory
{
	/**
	 * Default slot max count
	 */
	protected final int slots;
	/**
	 * Access able slots side all
	 */
	protected int[] openSlots;
	/**
	 * Items contained in this inv
	 */
	protected ItemStack[] containedItems;
	/**
	 * Host tileEntity
	 */
	protected IInventoryProvider host;

	public ExternalInventory(IInventoryProvider inv, int slots)
	{
		this.host = inv;
		this.slots = slots;
	}

	protected TileEntity tile()
	{
		return (TileEntity)host;
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
		ItemStack pre_stack = this.getContainedItems()[slot] != null ? this.getContainedItems()[slot].copy() : null;
		this.getContainedItems()[slot] = insertStack;

		if (insertStack != null && insertStack.stackSize > this.getInventoryStackLimit())
		{
			insertStack.stackSize = this.getInventoryStackLimit();
		}
		if (!InventoryUtility.stacksMatchExact(pre_stack, getContainedItems()[slot]))
		{
			markDirty();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "container.chest";
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
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		if (host instanceof TileEntity)
		{
			return tile().getWorldObj().getTileEntity(tile().xCoord, tile().yCoord, tile().zCoord) != tile() ? false : par1EntityPlayer.getDistanceSq(tile().xCoord + 0.5D, tile().yCoord + 0.5D, tile().zCoord + 0.5D) <= 64.0D;
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
