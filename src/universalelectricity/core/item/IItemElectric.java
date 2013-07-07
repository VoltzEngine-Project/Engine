package universalelectricity.core.item;

import net.minecraft.item.ItemStack;

public interface IItemElectric
{
	/**
	 * Adds energy to an item. Returns the quantity of energy that was accepted. This should always
	 * return 0 if the item cannot be externally charged.
	 * 
	 * @param theItem ItemStack to be charged.
	 * @param energy Maximum amount of energy to be sent into the item.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the item.
	 */
	public float receiveEnergy(ItemStack theItem, float energy, boolean doReceive);

	/**
	 * Removes energy from an item. Returns the quantity of energy that was removed. This should
	 * always return 0 if the item cannot be externally discharged.
	 * 
	 * @param theItem ItemStack to be discharged.
	 * @param energy Maximum amount of energy to be removed from the item.
	 * @param doTransfer If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the item.
	 */
	public float transferEnergy(ItemStack theItem, float energy, boolean doTransfer);

	/**
	 * Get the amount of energy currently stored in the item.
	 */
	public float getEnergyStored(ItemStack theItem);

	/**
	 * Get the max amount of energy that can be stored in the item.
	 */
	public float getMaxEnergyStored(ItemStack theItem);

	/**
	 * Sets the amount of energy in the ItemStack.
	 * 
	 * @param itemStack - the ItemStack.
	 * @param joules - Amount of energy.
	 */
	public void setEnergy(ItemStack itemStack, float joules);

	/**
	 * @return the energy request this ItemStack demands.
	 */
	public float getTransfer(ItemStack itemStack);

	/**
	 * @return The voltage in which this item runs on.
	 */
	public float getVoltage(ItemStack itemStack);
}
