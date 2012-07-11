package universalelectricity.extend;


/**
 * The Interface IRedstoneProvider.
 */
public interface IRedstoneProvider
{
    
    /**
     * Checks if is powering to.
     *
     * @param byte0 the byte0
     * @return true, if is powering to
     */
    public abstract boolean isPoweringTo(byte byte0);

    /**
     * Checks if is indirectly powering to.
     *
     * @param byte0 the byte0
     * @return true, if is indirectly powering to
     */
    public abstract boolean isIndirectlyPoweringTo(byte byte0);
}
