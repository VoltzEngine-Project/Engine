package universalelectricity.api.energy;

import net.minecraftforge.common.ForgeDirection;

/**
 * Applied to all TileEntities that can interact with energy.
 * 
 * @author Calclavia, Inspired by King_Lemming
 * 
 */
public interface IEnergyInterface
{
	/**
	 * Adds energy to an block. Returns the quantity of energy that was accepted. This
	 * should always return 0 if the block cannot be externally charged.
	 * 
	 * @param from Orientation the energy is sent in from.
	 * @param receive Maximum amount of energy (joules) to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive);

	/**
	 * Adds energy to an block. Returns the energyPack, the energy provided. This
	 * should always return null if the block cannot be externally discharged.
	 * 
	 * @param from Orientation the energy is requested from.
	 * @param energy Maximum amount of energy to be sent into the block.
	 * @param doExtract If false, the charge will only be simulated.
	 * @return Amount of energy that was given out by the block.
	 */
	public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract);

	/**
	 * Can this TileEntity connect with another?
	 * 
	 * @return Return true, if the connection is possible.
	 */
	public boolean canConnect(ForgeDirection direction);

}
