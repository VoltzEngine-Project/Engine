package universalelectricity.implement;

public interface IConductor
{
	/**
     * Gets the resistance of the conductor. Used to calculate energy loss.
     * A higher resistance means a higher energy loss.
     * @return The amount of Ohm's of resistance.
     */
    public abstract double getResistance();
    
    /**
     * The maximum amount of voltage this conductor can handle before exploding
     * @return The amount of voltage in volts
     */
    public abstract double getMaxVoltage();
}
