package com.builtbroken.mc.api.tile.node;

import com.builtbroken.mc.api.ISave;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * @author DarkGuardsman
 */
public interface IExternalInventory extends IInventory
{
	/**
	 * Gets the inventory array. ForgeDirection.UNKOWN must return all sides
	 */
	ItemStack[] getContainedItems();

	/**
	 * Deletes all the items in the inventory
	 */
	void clear();
}
