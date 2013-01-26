package universalelectricity.core.implement;

import universalelectricity.core.electricity.ElectricityNetwork;

public interface INetworkProvider
{
	/**
	 * The electrical network this conductor is on.
	 */
	public ElectricityNetwork getNetwork();

	public void setNetwork(ElectricityNetwork network);
}
