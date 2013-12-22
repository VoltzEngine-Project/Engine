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
	 * Gets the amount of energy loss through conducting pass this conductor.
	 * 
	 * @return The amount of loss in joules.
	 */
	public long getEnergyLoss();

	/**
	 * The maximum amount of energy this conductor can buffer (the transfer rate,
	 * essentially).
	 * 
	 * @return The amount of energy in joules.
	 */
	public long getEnergyCapacitance();

}
