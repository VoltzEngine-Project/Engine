package net.minecraft.src.universalelectricity.electricity;

import net.minecraft.src.universalelectricity.extend.IDisableable;

/**
 * The UEIConsumer interface is an interface that must be applied to all tile entities that can receive electricity.
 * @author Calclavia
 *
 */
public interface IElectricUnit extends IDisableable
{	
	/**
	 * Called every time a tick interval (specified in getConsumerTickInterval()).
	 * @param watts - Amount of watts this electric unit is receiving. Won't exceed the electricity need.
	 * @param voltage - The voltage of the electricity sent
	 * @param side - The side of the block in which the electricity is coming from
	 */
	public void onUpdate(float watts, float voltage, byte side);
	
	/**
	 * How much electricity this electric unit needs/wants. Recommend for you
	 * to return the max electricity storage of this machine (if there is one).
	 */
	public float electricityRequest();
	
	/**
	 * Can this machine visually connect to a wire on this specific side?
	 * @param side. 0-5 byte
	 * @return - True if so.
	 */
	public boolean canConnect(byte side);
	
	/**
	 * Can this unit receive electricity from this specific side
	 * @param side. 0-5 byte
	 * @return - True if so.
	 */
	public boolean canReceiveFromSide(byte side);
	
	/**
	 * Gets the voltage of the electricity consumer. Used in a conductor to find the voltage difference.
	 * If the voltage is too high, things might explode.
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public float getVoltage();
	
	/*---------------- Settings ----------------*/
	/**
	 * How many world ticks there should be before this tile entity gets ticked?
	 * E.x Returning "1" will make this tile entity tick every single tick, "20"
	 * will make this tile entity tick every second.
	 * Using a higher number will result in less lag.
	 * @return - The tick intervals. Returns 0 if you wish it to not tick at all.
	 */
	public int getTickInterval();
}