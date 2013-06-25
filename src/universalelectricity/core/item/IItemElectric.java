package universalelectricity.core.item;

import net.minecraft.item.ItemStack;
import thermalexpansion.api.item.IChargeableItem;

public interface IItemElectric extends IChargeableItem
{
	/**
	 * Sets the amount of energy in the ItemStack.
	 * 
	 * @param itemStack - the ItemStack.
	 * @param joules - Amount of energy.
	 */
	public void setEnergy(ItemStack itemStack, float joules);

	/**
	 * @return The voltage in which this item runs on.
	 */
	public float getVoltage(ItemStack itemStack);
}
