package universalelectricity.api;

import net.minecraft.tileentity.TileEntity;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia
 * 
 */
public interface IConnector<N> extends IGridProvider<N>
{
	/**
	 * Gets a list of all the connected connectors that this conductor is connected to.
	 * 
	 * @return An array of length "6".
	 */
	public TileEntity[] getAdjacentConnections();
}
