package universalelectricity.core.block;

import buildcraft.api.power.IPowerReceptor;

public interface IConductor extends INetworkProvider, IConnectionProvider, IPowerReceptor
{
	/**
	 * Gets the resistance of the conductor. Used to calculate energy loss. A higher resistance
	 * means a higher energy loss.
	 * 
	 * @return The amount of resistance in Ohms.
	 */
	public float getResistance();

	/**
	 * @return The maximum amount of amps this conductor can handle before melting down.
	 */
	public float getCurrentCapcity();
}
