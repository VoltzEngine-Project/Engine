package universalelectricity.prefab.conductor;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.implement.INetworkProvider;

/**
 * Must be applied to all tile entities that are conductors.
 * 
 * @author Calclavia
 * 
 */
public interface IConductor extends INetworkProvider
{
	/**
	 * The UE tile entities that this conductor is connected to.
	 * 
	 * @return
	 */
	public TileEntity[] getConnectedBlocks();

	/**
	 * Gets the resistance of the conductor. Used to calculate energy loss. A higher resistance
	 * means a higher energy loss.
	 * 
	 * @return The amount of resistance in Ohms.
	 */
	public double getResistance();

	/**
	 * The maximum amount of amps this conductor can handle before melting down. This is calculating
	 * PER TICK!
	 * 
	 * @return The amount of amps in volts
	 */
	public double getMaxAmps();

	/**
	 * Called when the electricity passing through exceeds the maximum voltage.
	 */
	public void onOverCharge();

	/**
	 * Resets the conductor
	 */
	public void reset();

	/**
	 * Instantly refreshes all connected blocks
	 */
	public void refreshConnectedBlocks();

	/**
	 * Adds a connection between this conductor and a UE unit
	 * 
	 * @param tileEntity - Must be either a producer, consumer or a conductor
	 * @param side - side in which the connection is coming from
	 */
	public void updateConnection(TileEntity tileEntity, ForgeDirection side);

	public void updateConnectionWithoutSplit(TileEntity connectorFromSide, ForgeDirection orientation);
}
