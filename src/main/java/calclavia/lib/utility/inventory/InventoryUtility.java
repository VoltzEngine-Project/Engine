package calclavia.lib.utility.inventory;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;

/**
 * @author Calclavia
 * 
 */
public class InventoryUtility
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

	public static void dropBlockAsItem(World world, Vector3 position)
	{
		if (!world.isRemote)
		{
			int meta = position.getBlockMetadata(world);
			int id = position.getBlockID(world);
			ArrayList<ItemStack> items = Block.blocksList[id].getBlockDropped(world, position.intX(), position.intY(), position.intZ(), meta, 0);

			for (ItemStack itemStack : items)
			{
				dropItemStack(world, position, itemStack);
			}
		}
	}

	/**
	 * Drops an item stack on the floor.
	 */
	public static void dropItemStack(World world, Vector3 position, ItemStack itemStack)
	{
		dropItemStack(world, position, itemStack, 10);
	}

	public static void dropItemStack(World world, Vector3 position, ItemStack itemStack, int delay)
	{
		if (!world.isRemote)
		{
			float f = 0.7F;
			double randomX = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double randomY = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double randomZ = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, position.x + randomX, position.y + randomY, position.z + randomZ, itemStack);
			entityitem.delayBeforeCanPickup = delay;
			world.spawnEntityInWorld(entityitem);
		}
	}
}
