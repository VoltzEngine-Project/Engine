package universalelectricity.prefab.tile;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.implement.IConnector;
import universalelectricity.core.implement.IVoltage;

/**
 * An easier way to implement the methods from IElectricityReceiver with default values set.
 * 
 * @author Calclavia
 */
public abstract class TileEntityElectricityProducer extends TileEntityDisableable implements IConnector, IVoltage
{
	public TileEntityElectricityProducer()
	{
		super();
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
	}

	@Override
	public boolean canConnect(ForgeDirection side)
	{
		return true;
	}

	@Override
	public double getVoltage()
	{
		return 120;
	}

	@Override
	public void invalidate()
	{
		Electricity.instance.unregister(this);
	}
}
