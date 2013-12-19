package universalelectricity.api;

import net.minecraftforge.common.ForgeDirection;

/**
 * This interface is to be applied to all TileEntities which stores electricity within them.
 * 
 * @author Calclavia
 */
public interface IElectricalStorage
{
	/**
	 * Sets the amount of joules this unit has stored.
	 */
	public void setEnergyStored(float energy);

	/**
	 * * @return Get the amount of energy currently stored in the block.
	 */
	public float getEnergyStored(ForgeDirection from);

	/**
	 * @return Get the max amount of energy that can be stored in the block.
	 */
	public float getMaxEnergyStored(ForgeDirection from);
}
