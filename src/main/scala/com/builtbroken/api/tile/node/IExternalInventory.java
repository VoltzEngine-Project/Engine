package com.builtbroken.api.tile.node;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import com.builtbroken.lib.utility.nbt.ISaveObj;

/**
 * @author DarkGuardsman
 */
public interface IExternalInventory extends ISidedInventory, ISaveObj
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
