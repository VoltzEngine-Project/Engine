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
	 * Called for the conductor to distribute energy. The conductor should distribute its internal
	 * buffer to all {IEnergyInterface} connected to it.
	 */
	public void distribute();

	/**
	 * Gets the amount of energy loss through conducting pass this conductor.
	 * 
	 * @return The amount of loss in joules.
	 */
	public int getEnergyLoss();

	/**
	 * The maximum amount of energy this conductor can buffer (the transfer rate,
	 * essentially).
	 * 
	 * @return The amount of energy in joules.
	 */
	public int getEnergyCapacitance();

}
