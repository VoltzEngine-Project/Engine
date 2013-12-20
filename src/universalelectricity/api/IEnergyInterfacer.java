package universalelectricity.api;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityPack;

/**
 * Applied to all TileEntities that can interact with energy.
 * 
 * @author Calclavia, Inspired by King_Lemming
 * 
 */
public interface IEnergyInterfacer extends IConnector
{
	/**
	 * Adds electricity to an block. Returns the quantity of electricity that was accepted. This
	 * should always return 0 if the block cannot be externally charged.
	 * 
	 * @param from Orientation the electricity is sent in from.
	 * @param receive Maximum amount of electricity (joules) to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	public int onReceiveEnergy(ForgeDirection from, int receive, boolean doReceive);

	/**
	 * Adds electricity to an block. Returns the ElectricityPack, the electricity provided. This
	 * should always return null if the block cannot be externally discharged.
	 * 
	 * @param from Orientation the electricity is requested from.
	 * @param energy Maximum amount of energy to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was given out by the block.
	 */
	public int onExtractEnergy(ForgeDirection from, int request, boolean doProvide);

	/**
	 * Gets the voltage of this TileEntity.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public int getVoltage(ForgeDirection direction);

}
