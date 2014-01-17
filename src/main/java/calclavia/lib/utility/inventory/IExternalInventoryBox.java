package calclavia.lib.utility.inventory;

import calclavia.lib.utility.nbt.ISaveObj;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/** @author DarkGuardsman */
public interface IExternalInventoryBox extends ISidedInventory, ISaveObj
{
	/** Gets the inventory array. ForgeDirection.UNKOWN must return all sides */
	public ItemStack[] getContainedItems();

	/** Dels all the items in the inventory */
	public void clear();
}
