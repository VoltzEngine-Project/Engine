package universalelectricity.core.block;

import universalelectricity.core.electricity.ElectricityPack;

/**
 * Applied to all TileEntities that can interact with electricity.
 * 
 * @author Calclavia, King_Lemming
 * 
 */
public interface IElectrical extends IConnector
{
	/**
	 * Adds energy to an block. Returns the quantity of energy that was accepted. This should always
	 * return 0 if the block cannot be externally charged.
	 * 
	 * @param energy Maximum amount of energy to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	public float receiveElectricity(ElectricityPack electricityPack, boolean doReceive);

	/**
	 * @return How much energy does this TileEntity want?
	 */
	public float getRequest();

	/**
	 * Gets the voltage of this object.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public float getVoltage();

}
