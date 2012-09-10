package universalelectricity.extend;

/**
 * This interface is to be applied to all tile entities which stores electricity
 * within them.
 * @author Calclavia
 *
 */
public interface IElectricityStorage
{
	/**
	 * Returns the amount of Watt-hours this unit has stored.
	 */
	public float getWattHours();
	
	/**
	 * Sets the amount of Watt-hours this unit has stored.
	 */
	public void setWattHours(float wattHours);
}
