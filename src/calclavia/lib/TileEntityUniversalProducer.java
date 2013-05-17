package calclavia.lib;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.EnumSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectrical;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import cpw.mods.fml.common.Loader;

public class TileEntityUniversalProducer extends TileEntityElectrical implements IEnergySource, IPowerReceptor
{
	private IPowerProvider powerProvider;

	public TileEntityUniversalProducer()
	{
		if (PowerFramework.currentFramework != null)
		{
			if (this.powerProvider == null)
			{
				this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
				this.powerProvider.configure(0, 0, 0, 0, Integer.MAX_VALUE);
			}
		}
	}

	@Override
	public void initiate()
	{
		super.initiate();
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
	}

	@Override
	public void invalidate()
	{
		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
		super.invalidate();
	}

	public ElectricityPack produce(double watts)
	{
		ElectricityPack pack = new ElectricityPack(watts / this.getVoltage(), this.getVoltage());
		ElectricityPack remaining = ElectricityNetworkHelper.produceFromMultipleSides(this, pack);

		/**
		 * Try outputting BuildCraft power.
		 */
		if (remaining.getWatts() > 0)
		{
			EnumSet<ForgeDirection> approachingDirections = ElectricityNetworkHelper.getDirections(this);

			for (ForgeDirection direction : approachingDirections)
			{
				TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), direction);

				if (this.getPowerProvider(tileEntity) != null)
				{
					this.getPowerProvider(tileEntity).receiveEnergy((float) (remaining.getWatts() * UniversalElectricity.TO_BC_RATIO), direction.getOpposite());
				}
			}
		}

		if (Loader.isModLoaded("IC2") && remaining.getWatts() > 0)
		{
			EnergyTileSourceEvent evt = new EnergyTileSourceEvent(this, (int) (remaining.getWatts() * UniversalElectricity.TO_IC2_RATIO));
			MinecraftForge.EVENT_BUS.post(evt);
			remaining = new ElectricityPack((evt.amount * UniversalElectricity.IC2_RATIO) / remaining.voltage, remaining.voltage);
		}

		return remaining;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		if (this instanceof IRotatable)
		{
			return direction.ordinal() == this.getBlockMetadata();
		}

		return true;
	}

	public IPowerProvider getPowerProvider(TileEntity tileEntity)
	{
		if (tileEntity instanceof IPowerReceptor)
		{
			return ((IPowerReceptor) tileEntity).getPowerProvider();
		}

		return null;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return this.canConnect(direction.toForgeDirection());
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.ticks > 0;
	}

	@Override
	public int getMaxEnergyOutput()
	{
		return 2048;
	}

	/**
	 * Buildcraft
	 */
	@Override
	public void setPowerProvider(IPowerProvider provider)
	{
		this.powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider()
	{
		return this.powerProvider;
	}

	@Override
	public void doWork()
	{

	}

	@Override
	public int powerRequest(ForgeDirection from)
	{
		return 0;
	}
}
