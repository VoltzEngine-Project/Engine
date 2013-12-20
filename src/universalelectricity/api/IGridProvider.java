package universalelectricity.api;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia
 * 
 */
public interface IGridProvider<N>
{
	public N getNetwork();

	public void setNetwork(N network);
}
