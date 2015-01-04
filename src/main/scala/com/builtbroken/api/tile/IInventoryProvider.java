package com.builtbroken.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.api.tile.node.IExternalInventory;

/** Used with IExternalInventory to move the inventory functionality
 * outside of the tile class. Designed to make it easier to abstract inventory
 * functionality. Though for ISidedInventory it still requires the methods
 * to be present in the tile. Though this can easily be achieved with compile
 * time injection of methods, asm, copy/paste, or scala traits.
 *
 * @author Darkguardsman
 */
public interface IInventoryProvider
{
    /** External inventory object */
	public IExternalInventory getInventory();

    /** Call back for IExternalInventory to check if the item can be stored */
	public boolean canStore(ItemStack stack, int slot, ForgeDirection side);

    /** Call back for IExternalInventory to check if the item can be removed */
	public boolean canRemove(ItemStack stack, int slot, ForgeDirection side);
}
