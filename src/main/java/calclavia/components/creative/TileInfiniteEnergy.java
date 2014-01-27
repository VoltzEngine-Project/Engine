package calclavia.components.creative;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.energy.EnergyStorageHandler;
import calclavia.lib.prefab.tile.TileElectrical;

public class TileInfiniteEnergy extends TileElectrical
{
	public TileInfiniteEnergy()
	{
		this.energy = new EnergyStorageHandler(Long.MAX_VALUE);
		this.energy.setMaxExtract(Long.MAX_VALUE);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		this.energy.setEnergy(Long.MAX_VALUE);
		this.produce();
	}

	@Override
	public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
	{
		return receive;
	}

	@Override
	public long onExtractEnergy(ForgeDirection from, long request, boolean doExtract)
	{
		return request;
	}
}
