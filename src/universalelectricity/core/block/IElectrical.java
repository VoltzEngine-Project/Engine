package universalelectricity.core.block;

import universalelectricity.core.electricity.ElectricityPack;

/**
 * Applied to all TileEntities that can interact with electricity.
 * 
 * @author Calclavia, King_Lemming
 * 
 */
public interface IElectrical extends IVoltage, IConnector
{
	/**
	 * Adds energy to an block. Returns the quantity of energy that was accepted. This should always
	 * return 0 if the block cannot be externally charged.
	 * 
	 * @param energy Maximum amount of energy to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	public ElectricityPack receiveEnergy(ElectricityPack electricityPack, boolean doReceive);

	/**
	 * Removes energy from an block. Returns the quantity of energy that was removed. This should
	 * always return 0 if the block cannot be externally discharged.
	 * 
	 * @param energy Maximum amount of energy to be removed from the block.
	 * @param doTransfer If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the block.
	 */
	public ElectricityPack provideEnergy(ElectricityPack electricityPack, boolean doTransfer);

	/**
	 * @return How much energy does this TileEntity want?
	 */
	public float getRequest();

	/**
	 * @return Get the amount of energy currently stored in the block.
	 */
	public float getEnergyStored();

	/**
	 * @return Get the max amount of energy that can be stored in the block.
	 */
	public float getMaxEnergyStored();
}
