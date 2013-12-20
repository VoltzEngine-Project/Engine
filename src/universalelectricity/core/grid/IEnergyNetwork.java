package universalelectricity.core.grid;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.energy.IConductor;
import universalelectricity.core.electricity.ElectricityPack;

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
	public int getDistribution(IConductor conductor);

	/**
	 * Gets the amount of distributed energy per side.
	 * 
	 * @return
	 */
	public int getDistributionSide(IConductor conductor);

	/**
	 * @return The total amount of resistance of this electrical network. In Ohms.
	 */
	public int getTotalEnergyLoss();
}
