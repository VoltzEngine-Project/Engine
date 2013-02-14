package universalelectricity.core.implement;

import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;

public interface IItemElectric
{
	/**
	 * Called when this item receives electricity.
	 * 
	 * @inputRequest The amount of electricity being input.
	 * @return The amount of electricity being rejected.
	 */
	public ElectricityPack onReceive(ElectricityPack inputRequest, ItemStack itemStack);

	/**
	 * Called when something requests electricity from this item.
	 * 
	 * @return - The amount of electricity given out.
	 */
	public ElectricityPack onRequest(ElectricityPack outputRequest, ItemStack itemStack);

	/**
	 * Returns the amount of joules this electric item stored.
	 */
	public double getJoules(ItemStack itemStack);

	/**
	 * Sets the amount of joules this electric item stored. Mods should called onReceive and onUse
	 * instead of this in most cases.
	 */
	public void setJoules(double joules, ItemStack itemStack);

	/**
	 * Gets the maximum amount of joules this unit can store.
	 */
	public double getMaxJoules(ItemStack itemStack);

	/**
	 * Gets the voltage of this object.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public double getVoltage(ItemStack itemStack);

	/**
	 * Gets the transfer rate of this electric item. Used for recharging the item in battery
	 * components.
	 * 
	 */
	public ElectricityPack getTransferRate(ItemStack itemStack);
}
