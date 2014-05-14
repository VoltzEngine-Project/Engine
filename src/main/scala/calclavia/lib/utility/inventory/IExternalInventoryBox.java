package calclavia.lib.utility.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import calclavia.lib.utility.nbt.ISaveObj;

/** @author DarkGuardsman */
public interface IExternalInventoryBox extends ISidedInventory, ISaveObj
{
    /** Gets the inventory array. ForgeDirection.UNKOWN must return all sides */
    public ItemStack[] getContainedItems();

    /** Dels all the items in the inventory */
    public void clear();
}
