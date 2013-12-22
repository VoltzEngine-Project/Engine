package universalelectricity.api.net;

import net.minecraft.tileentity.TileEntity;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia
 * 
 */
public interface IConnector<N> extends INetworkProvider<N>, IConnectable
{
	/**
	 * Gets an array of all the connected IConnectors that this conductor is connected to.
	 * 
	 * @return An array of length "6".
	 */
	public Object[] getConnections();
}
