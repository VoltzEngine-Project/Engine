package calclavia.components.creative;

import calclavia.lib.prefab.tile.TileElectrical;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.energy.EnergyStorageHandler;

/**
 * Creative mode way to quickly debug machines that use or produce electrical energy
 *
 * @author DarkGuardsman
 */
public class TileInfiniteEnergy extends TileElectrical
{
	public TileInfiniteEnergy()
	{
		setEnergyHandler(new EnergyStorageHandler(Long.MAX_VALUE));
		getEnergyHandler().setMaxExtract(Long.MAX_VALUE);
		ioMap = 728;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		getEnergyHandler().setEnergy(Long.MAX_VALUE);
		produce();
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
