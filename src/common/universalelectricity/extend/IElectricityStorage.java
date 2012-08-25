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
	 * Returns the amount of Amp-hours this unit has stored.
	 */
	public float getAmpHours();
	
	/**
	 * Sets the amount of Amp-hours this unit has stored.
	 */
	public void setAmpHours(float AmpHours);
}
