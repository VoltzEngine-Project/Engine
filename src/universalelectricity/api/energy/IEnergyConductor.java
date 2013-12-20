package universalelectricity.api.energy;

import universalelectricity.api.IConnector;
import universalelectricity.core.grid.IEnergyNetwork;

/**
 * A connector for {EnergyNetwork}.
 * 
 * @author Calclavia
 * 
 */
public interface IEnergyConductor extends IConnector<IEnergyNetwork>, IEnergyInterface
{
	/**
	 * Refreshes the conductor
	 */
	public void refresh();

	/**
	 * Gets the amount of energy loss through conducting pass this wire.
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
