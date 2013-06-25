package universalelectricity.core.block;

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
	public float receiveEnergy(float energy, boolean doReceive);

	/**
	 * Removes energy from an block. Returns the quantity of energy that was removed. This should
	 * always return 0 if the block cannot be externally discharged.
	 * 
	 * @param energy Maximum amount of energy to be removed from the block.
	 * @param doTransfer If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the block.
	 */
	public float transferEnergy(float energy, boolean doTransfer);

	/**
	 * @return Get the amount of energy currently stored in the block.
	 */
	public float getEnergyStored();

	/**
	 * @return Get the max amount of energy that can be stored in the block.
	 */
	public float getMaxEnergyStored();
}
