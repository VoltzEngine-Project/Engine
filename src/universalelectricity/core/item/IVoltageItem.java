package universalelectricity.core.item;

import net.minecraft.item.ItemStack;

/**
 * @author Calclavia
 * 
 */
public interface IVoltageItem
{
	/**
	 * Get the max amount of voltage of this item.
	 */
	public long getVoltage(ItemStack theItem);
}
