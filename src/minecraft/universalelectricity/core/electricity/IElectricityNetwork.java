package universalelectricity.core.electricity;

import java.util.HashMap;
import java.util.List;

import universalelectricity.core.block.IConductor;

import net.minecraft.tileentity.TileEntity;

/**
 * The Electrical Network in interface form.
 * 
 * @author Calclavia
 * 
 */
public interface IElectricityNetwork
{
	public void startProducing(TileEntity tileEntity, ElectricityPack electricityPack);

	public void startProducing(TileEntity tileEntity, double amperes, double voltage);

	public boolean isProducing(TileEntity tileEntity);

	public void stopProducing(TileEntity tileEntity);

	public void startRequesting(TileEntity tileEntity, ElectricityPack electricityPack);

	public void startRequesting(TileEntity tileEntity, double amperes, double voltage);

	public boolean isRequesting(TileEntity tileEntity);

	public void stopRequesting(TileEntity tileEntity);

	public ElectricityPack getProduced(TileEntity... ignoreTiles);

	public ElectricityPack getRequest(TileEntity... ignoreTiles);

	public ElectricityPack getRequestWithoutReduction();

	public ElectricityPack consumeElectricity(TileEntity tileEntity);

	public HashMap<TileEntity, ElectricityPack> getProducers();

	public HashMap<TileEntity, ElectricityPack> getConsumers();

	public List<TileEntity> getProviders();

	public List<TileEntity> getReceivers();

	/**
	 * @return A list of all conductors in this electrical network.
	 */
	public List<IConductor> getConductors();

	/**
	 * @return The total amount of resistance of this electrical network. In Ohms.
	 */
	public double getTotalResistance();

	/**
	 * @return The lowest amount of current (amperage) that this electrical network can tolerate.
	 */
	public double getLowestCurrentCapacity();

	/**
	 * Merges another electrical network into this one, setting the other network into null.
	 * 
	 * @param network
	 */
	public void mergeConnection(IElectricityNetwork network);

	/**
	 * Refreshes and recalculates wire connections in this electrical network.
	 * 
	 * @param doSplit - True if check for connection splits.
	 */
	public void refreshConductors(boolean doSplit);

}
