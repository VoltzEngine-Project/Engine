package universalelectricity.core.implement;

import net.minecraft.item.ItemStack;

public interface IItemElectric
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
	
	/**
	 * 
	 * @return The rate in which this electric item transfers its electricity in joules.
	 */
	public double getTransferRate();

	/**
	 * Returns the amount of joules this unit has stored.
	 */
	public double getJoules(ItemStack itemStack);

	/**
	 * Sets the amount of joules this unit has stored.
	 */
	public void setJoules(double joules, ItemStack itemStack);

	/**
	 * Gets the maximum amount of joules this unit can store. This may possibly have an argument of
	 * null when the item is initiated so watch out!
	 */
	public double getMaxJoules(ItemStack itemStack);

	/**
	 * Gets the voltage of this object.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public double getVoltage(ItemStack itemStack);
}
