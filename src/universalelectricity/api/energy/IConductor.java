package universalelectricity.api.energy;

import universalelectricity.api.net.IConnector;

/**
 * A connector for {EnergyNetwork}.
 * 
 * @author Calclavia
 * 
 */
public interface IConductor extends IConnector<IEnergyNetwork>, IEnergyInterface
{
	/**
	 * Gets the amount of resistance of energy conducting pass this conductor.
	 * 
	 * @return The amount of loss in Ohms.
	 */
	public float getResistance();

	/**
	 * The maximum amount of energy this conductor can buffer (the transfer rate,
	 * essentially).
	 * 
	 * @return The amount of energy in joules.
	 */
	public long getTransferCapacity();

}
