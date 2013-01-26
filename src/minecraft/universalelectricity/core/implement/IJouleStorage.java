package universalelectricity.core.implement;

/**
 * This interface is to be applied to all tile entities which stores energy within them.
 * 
 * @author Calclavia
 */
public interface IJouleStorage
{
	/**
	 * Returns the amount of joules this unit has stored.
	 */
	public double getJoules();

	/**
	 * Sets the amount of joules this unit has stored.
	 */
	public void setJoules(double joules);

	/**
	 * Gets the maximum amount of joules this unit can store.
	 */
	public double getMaxJoules();
}
