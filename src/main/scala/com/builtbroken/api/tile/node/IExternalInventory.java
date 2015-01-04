package com.builtbroken.api.tile.node;

import com.builtbroken.api.ISave;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * @author DarkGuardsman
 */
public interface IExternalInventory extends ISidedInventory, ISave
{
	/**
	 * Gets the inventory array. ForgeDirection.UNKOWN must return all sides
	 */
	public ItemStack[] getContainedItems();

	/**
	 * Deletes all the items in the inventory
	 */
	public void clear();
}
