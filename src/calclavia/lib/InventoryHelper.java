package calclavia.lib;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author Calclavia
 * 
 */
public class InventoryHelper
{
	public static IInventory checkChestInv(IInventory inv)
	{
		if (inv instanceof TileEntityChest)
		{
			TileEntityChest main = (TileEntityChest) inv;
			TileEntityChest adj = null;

			if (main.adjacentChestXNeg != null)
			{
				adj = main.adjacentChestXNeg;
			}
			else if (main.adjacentChestXPos != null)
			{
				adj = main.adjacentChestXPos;
			}
			else if (main.adjacentChestZNeg != null)
			{
				adj = main.adjacentChestZNeg;
			}
			else if (main.adjacentChestZPosition != null)
			{
				adj = main.adjacentChestZPosition;
			}

			if (adj != null)
			{
				return new InventoryLargeChest("", main, adj);
			}
		}

		return inv;
	}

	public static ItemStack putStackInInventory(IInventory inventory, ItemStack itemStack, int side, boolean force)
	{
		ItemStack toInsert = itemStack.copy();

		if (!(inventory instanceof ISidedInventory))
		{
			inventory = checkChestInv(inventory);

			for (int i = 0; i <= inventory.getSizeInventory() - 1; i++)
			{
				if (!force)
				{
					if (!inventory.isItemValidForSlot(i, toInsert))
					{
						continue;
					}
				}

				ItemStack inSlot = inventory.getStackInSlot(i);

				if (inSlot == null)
				{
					inventory.setInventorySlotContents(i, toInsert);
					return null;
				}
				else if (inSlot.isItemEqual(toInsert) && inSlot.stackSize < inSlot.getMaxStackSize())
				{
					if (inSlot.stackSize + toInsert.stackSize <= inSlot.getMaxStackSize())
					{
						ItemStack toSet = toInsert.copy();
						toSet.stackSize += inSlot.stackSize;

						inventory.setInventorySlotContents(i, toSet);
						return null;
					}
					else
					{
						int rejects = (inSlot.stackSize + toInsert.stackSize) - inSlot.getMaxStackSize();

						ItemStack toSet = toInsert.copy();
						toSet.stackSize = inSlot.getMaxStackSize();

						ItemStack remains = toInsert.copy();
						remains.stackSize = rejects;

						inventory.setInventorySlotContents(i, toSet);

						toInsert = remains;
					}
				}
			}
		}
		else
		{
			ISidedInventory sidedInventory = (ISidedInventory) inventory;
			int[] slots = sidedInventory.getAccessibleSlotsFromSide(ForgeDirection.getOrientation(side).getOpposite().ordinal());

			if (slots != null && slots.length != 0)
			{
				for (int get = 0; get <= slots.length - 1; get++)
				{
					int slotID = slots[get];

					if (!force)
					{
						if (!sidedInventory.isItemValidForSlot(slotID, toInsert) && !sidedInventory.canInsertItem(slotID, toInsert, ForgeDirection.getOrientation(side).getOpposite().ordinal()))
						{
							continue;
						}
					}

					ItemStack inSlot = inventory.getStackInSlot(slotID);

					if (inSlot == null)
					{
						inventory.setInventorySlotContents(slotID, toInsert);
						return null;
					}
					else if (inSlot.isItemEqual(toInsert) && inSlot.stackSize < inSlot.getMaxStackSize())
					{
						if (inSlot.stackSize + toInsert.stackSize <= inSlot.getMaxStackSize())
						{
							ItemStack toSet = toInsert.copy();
							toSet.stackSize += inSlot.stackSize;

							inventory.setInventorySlotContents(slotID, toSet);
							return null;
						}
						else
						{
							int rejects = (inSlot.stackSize + toInsert.stackSize) - inSlot.getMaxStackSize();

							ItemStack toSet = toInsert.copy();
							toSet.stackSize = inSlot.getMaxStackSize();

							ItemStack remains = toInsert.copy();
							remains.stackSize = rejects;

							inventory.setInventorySlotContents(slotID, toSet);

							toInsert = remains;
						}
					}
				}
			}
		}

		return toInsert;
	}

	public static ItemStack takeTopItemFromInventory(IInventory inventory, int side)
	{
		if (!(inventory instanceof ISidedInventory))
		{
			for (int i = inventory.getSizeInventory() - 1; i >= 0; i--)
			{
				if (inventory.getStackInSlot(i) != null)
				{
					ItemStack toSend = inventory.getStackInSlot(i).copy();
					toSend.stackSize = 1;

					inventory.decrStackSize(i, 1);

					return toSend;
				}
			}
		}
		else
		{
			ISidedInventory sidedInventory = (ISidedInventory) inventory;
			int[] slots = sidedInventory.getAccessibleSlotsFromSide(side);

			if (slots != null)
			{
				for (int get = slots.length - 1; get >= 0; get--)
				{
					int slotID = slots[get];

					if (sidedInventory.getStackInSlot(slotID) != null)
					{
						ItemStack toSend = sidedInventory.getStackInSlot(slotID);
						toSend.stackSize = 1;

						if (sidedInventory.canExtractItem(slotID, toSend, side))
						{
							sidedInventory.decrStackSize(slotID, 1);

							return toSend;
						}
					}
				}
			}
		}

		return null;
	}
}
