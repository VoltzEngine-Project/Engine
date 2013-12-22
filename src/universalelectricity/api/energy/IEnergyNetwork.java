package universalelectricity.api.energy;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.net.INetwork;

/**
 * The Electrical Network in interface form.
 * 
 * @author Calclavia
 * 
 */
public interface IEnergyNetwork extends INetwork<IEnergyNetwork, IConductor, Object>
{
	/**
	 * Reconstructs the energy network.
	 */
	public void reconstruct();

	/**
	 * Produces power to the energy network.
	 * 
	 * @param receive - The amount that is produced.
	 * @return - The amount that was accepted by the network.
	 */
	public long produce(long receive);

	/**
	 * @return The last buffer in the network that was sent to all energy handlers.
	 */
	public long getLastBuffer();

	/**
	 * Iterates through all the energy handlers in the network and calculate the total energy
	 * request of the network. This method is slightly intensive and shouldn't be called often.
	 * Consider multi-threading.
	 * 
	 * @return Energy that is being request at this moment in the network.
	 */
	public long getRequest();
}
