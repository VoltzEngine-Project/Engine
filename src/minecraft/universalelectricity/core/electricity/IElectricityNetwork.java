package universalelectricity.core.electricity;

import java.util.HashMap;
import java.util.List;

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

}
