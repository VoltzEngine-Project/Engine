package resonant.api.items;

import net.minecraft.item.ItemStack;

/**
 * Applied to all items that has a frequency.
 *
 * @author Calclavia
 */

public interface IItemFrequency
{
	/**
	 * @return The frequency of this object.
	 */
	public int getFrequency(ItemStack itemStack);

	/**
	 * Sets the frequency
	 *
	 * @param frequency - The frequency of this object.
	 */
	public void setFrequency(int frequency, ItemStack itemStack);
}
