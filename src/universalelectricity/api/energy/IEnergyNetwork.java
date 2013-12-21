package universalelectricity.api.energy;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.INetwork;

/**
 * The Electrical Network in interface form.
 * 
 * @author Calclavia
 * 
 */
public interface IEnergyNetwork extends INetwork<IEnergyNetwork, IConductor, TileEntity>
{
	/**
	 * Gets the amount of distributed energy per conductor.
	 * 
	 * @return
	 */
	public long getDistribution(IConductor conductor);

	/**
	 * Gets the amount of distributed energy per side.
	 * 
	 * @return
	 */
	public long getDistributionSide(IConductor conductor);

	/**
	 * @return The total amount of resistance of this electrical network. In Ohms.
	 */
	public long getTotalEnergyLoss();
}
