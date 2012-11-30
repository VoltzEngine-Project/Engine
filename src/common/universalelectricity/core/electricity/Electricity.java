package universalelectricity.core.electricity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.src.TileEntity;

/**
 * THIS IS THE NEW ELECTRICITY MANAGER. THIS IS ONLY A DRAFT!
 * 
 * The Electricity Network Manager.
 * 
 * @author Calclavia
 * 
 */
public class Electricity
{
	public static final Electricity INSTANCE = new Electricity();

	private List<TileEntity> producers = new ArrayList<TileEntity>();
	private List<TileEntity> receivers = new ArrayList<TileEntity>();
	
	private HashMap<TileEntity, ElectricityPack> producerData = new HashMap<TileEntity, ElectricityPack>();

	public void registerProducer(TileEntity tileEntity)
	{
		if (!producers.contains(tileEntity))
			producers.add(tileEntity);
	}

	public void registerReceiver(TileEntity tileEntity)
	{
		if (!receivers.contains(tileEntity))
			receivers.add(tileEntity);
	}

	/**
	 * Once called, this electricity produce will start producing energy.
	 */
	public void startProducing(TileEntity tileEntity, double amperes, double voltage)
	{
		this.producerData.put(tileEntity, new ElectricityPack(amperes, voltage));
	}
	
	public void stopProducing(TileEntity tileEntity)
	{
		this.producerData.remove(tileEntity);
	}
}
