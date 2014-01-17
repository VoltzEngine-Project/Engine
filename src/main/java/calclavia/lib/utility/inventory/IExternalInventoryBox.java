package calclavia.lib.utility.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/** @author DarkGuardsman */
public interface IExternalInventoryBox extends ISidedInventory
{
	/** Gets the inventory array. ForgeDirection.UNKOWN must return all sides */
	public ItemStack[] getContainedItems();

	/** Called to save the inventory array */
	public NBTTagCompound save(NBTTagCompound tag);

	/** Called to load the inventory array */
	public void load(NBTTagCompound tag);

	/** Dels all the items in the inventory */
	public void clear();
}
