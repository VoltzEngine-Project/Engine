package basiccomponents;

import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;
import universalelectricity.extend.ItemElectric;


/**
 * The Class SlotElectricItem.
 */
public class SlotElectricItem extends Slot
{
    
    /**
     * Instantiates a new slot electric item.
     *
     * @param iinventory the iinventory
     * @param i the i
     * @param j the j
     * @param k the k
     */
    public SlotElectricItem(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     *
     * @param itemstack the itemstack
     * @return true, if is allowed
     */
    public boolean isAllowed(ItemStack itemstack)
    {
        return itemstack.getItem() instanceof ItemElectric;
    }
}
