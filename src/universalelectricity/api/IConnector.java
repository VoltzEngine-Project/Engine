package universalelectricity.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia
 * 
 */
public interface IConnector<N> extends INetworkProvider<N>
{
	/**
	 * Gets an array of all the connected TileEntity that this conductor is connected to.
	 * 
	 * @return An array of length "6".
	 */
	public TileEntity[] getAdjacentConnections();

	/**
	 * Can this TileEntity connect with another?
	 * 
	 * @return Return true, if the connection is possible.
	 */
	public boolean canConnect(ForgeDirection direction);
}
