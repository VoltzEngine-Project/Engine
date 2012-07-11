package universalelectricity.electricity;

import universalelectricity.extend.IDisableable;


/**
 * The Interface IElectricUnit.
 */
public interface IElectricUnit extends IDisableable
{
    
    /**
     * On update.
     *
     * @param f the f
     * @param f1 the f1
     * @param byte0 the byte0
     */
    public abstract void onUpdate(float f, float f1, byte byte0);

    /**
     * Electricity request.
     *
     * @return the float
     */
    public abstract float electricityRequest();

    /**
     * Can connect.
     *
     * @param byte0 the byte0
     * @return true, if successful
     */
    public abstract boolean canConnect(byte byte0);

    /**
     * Can receive from side.
     *
     * @param byte0 the byte0
     * @return true, if successful
     */
    public abstract boolean canReceiveFromSide(byte byte0);

    /**
     * Gets the voltage.
     *
     * @return the voltage
     */
    public abstract float getVoltage();

    /**
     * Gets the tick interval.
     *
     * @return the tick interval
     */
    public abstract int getTickInterval();
}
