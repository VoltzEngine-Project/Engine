package net.minecraft.src.universalelectricity;

/**
 * The UEIProducer interface is an interface that must be applied to all tile entities that can produce electricity.
 * @author Calclavia
 *
 */
public interface UEIProducer extends UEIDisableable
{
	/**
	 * onProduceElectricity is called when a conductor is connected to the producer block in which the conductor will demand power from the producer
	 * block.
	 * @param maxWatt - The maximum wattage the conductor can receive per tick
	 * @param voltage - The electric voltage in which the conductor connected to this producer is using
	 * @param side - The side of block in which the conductor is on
	 * @return watts - Return the amount of watts the conductor should receive from this producer
	 */
	public int onProduceElectricity(int maxWatts, int voltage, byte side);
	
	/**
	 * You can use this to check if a wire can connect to this UE consumer to properly render the graphics
	 * @param side - The side in which the electricity is coming from.
	 * @return Returns true or false if this consumer can receive electricity at this given tick or moment.
	 */
	public boolean canProduceElectricity(byte side);
	
	/**
	 * Gets the voltage of the electricity producer. Used in a conductor to find the voltage difference.
	 * If the voltage is too high, things might explode.
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public int getVolts();
}