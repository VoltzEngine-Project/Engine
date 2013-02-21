package universalelectricity.core.item;

/**
 * Applies to items that has a voltage.
 * 
 * @author Calclavia
 * 
 */
public interface IItemVoltage
{

	/**
	 * Gets the voltage of this object.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public double getVoltage();
}
