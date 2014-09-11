package resonant.lib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import resonant.lib.network.IPlayerUsing;

public class ContainerBase extends Container
{
	protected int slotCount = 0;
	protected int xInventoryDisplacement = 8;
	protected int yInventoryDisplacement = 135;
	protected int yHotBarDisplacement = 193;
	protected IInventory inventory;

	public ContainerBase(IInventory inventory)
	{
		this.inventory = inventory;
		this.slotCount = inventory.getSizeInventory();
	}

	public ContainerBase(EntityPlayer player, IInventory inventory)
	{
		this(inventory);

		if (inventory instanceof IPlayerUsing)
		{
			((IPlayerUsing) inventory).getPlayersUsing().add(player);
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		if (inventory instanceof IPlayerUsing)
		{
			((IPlayerUsing) inventory).getPlayersUsing().remove(entityplayer);
		}

		super.onContainerClosed(entityplayer);
	}

	public void addPlayerInventory(EntityPlayer player)
	{
		if (this.inventory instanceof IPlayerUsing)
		{
			((IPlayerUsing) this.inventory).getPlayersUsing().add(player);
		}

		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, this.xInventoryDisplacement + x * 18, this.yInventoryDisplacement + y * 18));
			}

		}

		for (int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(player.inventory, x, this.xInventoryDisplacement + x * 18, this.yHotBarDisplacement));
		}
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot_id)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slot_id);

		if (slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			itemstack = slotStack.copy();

			if (slot_id < this.slotCount)
			{
				/**
				 * The item is inside the block inventory, trying to move an item out.
				 */
				if (!mergeItemStack(slotStack, this.slotCount, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else
			{
				/**
				 * We are outside the block inventory, trying to move an item in.
				 *
				 * Only merge the inventory if it is valid for the specific slot!
				 */
				boolean foundValid = false;

				for (int i = 0; i < slotCount; i++)
				{
					if (inventory.isItemValidForSlot(i, slotStack))
					{
						if (!mergeItemStack(slotStack, i, i + 1, false))
						{
							return null;
						}

						foundValid = true;
					}
				}

				if(!foundValid)
					return null;
			}

			if (slotStack.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return this.inventory.isUseableByPlayer(entityplayer);
	}
}
