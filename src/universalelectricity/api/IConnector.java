package universalelectricity.api;

import net.minecraftforge.common.ForgeDirection;

/**
 * Applied to TileEntities that can connect to the network.
 * 
 * @author Calclavia
 * 
 */
public interface IConnector
{

	/**
	 * @return If the connection is possible.
	 */
	public boolean canConnect(ForgeDirection direction);
}
