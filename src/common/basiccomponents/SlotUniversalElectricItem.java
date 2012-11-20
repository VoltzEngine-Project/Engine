package basiccomponents;

import ic2.api.IElectricItem;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.SlotElectricItem;

/**
 * This slot should be used by any container that needs the slot for an electric item only.
 * 
 * @author Calclavia
 */
public class SlotUniversalElectricItem extends SlotElectricItem
{
	public SlotUniversalElectricItem(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return par1ItemStack.getItem() instanceof IItemElectric || par1ItemStack.getItem() instanceof IElectricItem;
	}
}
