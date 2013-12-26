package universalelectricity.api.net;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia
 * 
 */
public interface IConnector<N> extends INetworkProvider<N>, IConnectable
{
	/**
	 * Gets an array of all the connected IConnectors that this conductor is connected to. This
	 * should correspond to the ForgeDirection index.
	 * 
	 * @return An array of length "6".
	 */
	public Object[] getConnections();
}
