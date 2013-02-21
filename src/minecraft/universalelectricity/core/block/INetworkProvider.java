package universalelectricity.core.block;

import universalelectricity.core.electricity.ElectricityNetwork;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia
 * 
 */
public interface INetworkProvider
{
	public ElectricityNetwork getNetwork();

	public void setNetwork(ElectricityNetwork network);
}
