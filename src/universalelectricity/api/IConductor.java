package universalelectricity.api;

import net.minecraft.tileentity.TileEntity;

public interface IConductor extends INetworkProvider
{
	/**
	 * Gets a list of all the connected TileEntities that this conductor is connected to. The
	 * array's length should be always the 6 adjacent wires.
	 * 
	 * @return
	 */
	public TileEntity[] getAdjacentConnections();

	/**
	 * Refreshes the conductor
	 */
	public void refresh();

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
	public float getCurrentCapacity();
}
