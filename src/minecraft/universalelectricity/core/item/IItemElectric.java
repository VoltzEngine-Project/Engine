package universalelectricity.core.item;

import net.minecraft.item.ItemStack;

/**
 * An interface applied to all electrical items. Should be applied to the Item class.
 * 
 * @author Calclavia
 * 
 */
public interface IItemElectric extends IItemElectricityStorage, IItemVoltage
{
	/**
	 * Called when this item receives electricity.
	 */
	public double onReceive(double amps, double voltage, ItemStack itemStack);

	/**
	 * Called when something requests electricity from this item.
	 * 
	 * @return - The amount of given joules
	 */
	public double onUse(double joulesNeeded, ItemStack itemStack);

	/**
	 * @return Returns true or false if this consumer can receive electricity at this given tick or
	 * moment.
	 */
	public boolean canReceiveElectricity();

	/**
	 * Can this item give out electricity when placed in an tile entity? Electric items like
	 * batteries should be able to produce electricity (if they are rechargeable).
	 * 
	 * @return - True or False.
	 */
	public boolean canProduceElectricity();
}
