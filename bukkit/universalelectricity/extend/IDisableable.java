package universalelectricity.extend;


/**
 * The Interface IDisableable.
 */
public interface IDisableable
{
    
    /**
     * On disable.
     *
     * @param i the i
     */
    public abstract void onDisable(int i);

    /**
     * Checks if is disabled.
     *
     * @return true, if is disabled
     */
    public abstract boolean isDisabled();
}
