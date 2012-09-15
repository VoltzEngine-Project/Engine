package universalelectricity.extend;

/**
 * This interface is to be applied to all tile entities which stores electricity
 * within them.
 * @author Calclavia
 */
public interface IElectricityStorage
{
	/**
	 * Returns the amount of watt hours this unit has stored.
	 */
	public float getWattHours(Object... data);
	
	/**
	 * Sets the amount of watt hours this unit has stored.
	 */
	public void setWattHours(float wattHours, Object... data);
	
	/**
	 * Gets the maximum amount of watt hours this unit can store.
	 */
	public float getMaxWattHours();
}
