package calclavia.lib;

import ic2.api.Direction;
import ic2.api.IEnergyStorage;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.PowerFramework;

public abstract class TileEntityUniversalStorable extends TileEntityElectricityStorage implements IUniversalEnergyTile, IEnergyStorage
{
	private IPowerProvider powerProvider;

	public TileEntityUniversalStorable()
	{
		if (PowerFramework.currentFramework != null)
		{
			if (this.powerProvider == null)
			{
				this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
				this.powerProvider.configure(0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
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

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.powerProvider != null)
		{
			int requiredEnergy = (int) (this.getRequest().getWatts() * UniversalElectricity.TO_BC_RATIO);
			float energyReceived = this.powerProvider.useEnergy(requiredEnergy, requiredEnergy, true);
			this.onReceive(ElectricityPack.getFromWatts(UniversalElectricity.BC3_RATIO * energyReceived, this.getVoltage()));
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		if (this instanceof IRotatable)
		{
			return direction == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
		}

		return true;
	}

	public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z)
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata());
	}

	public void setDirection(World world, int x, int y, int z, ForgeDirection facingDirection)
	{
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal(), 2);
	}

	/**
	 * IC2
	 */
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		if (this.getConsumingSides() != null)
		{
			return this.getConsumingSides().contains(direction.toForgeDirection());
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.ticks > 0;
	}

	@Override
	public int demandsEnergy()
	{
		return (int) (this.getRequest().getWatts() * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int injectEnergy(Direction direction, int i)
	{
		double givenElectricity = i * UniversalElectricity.IC2_RATIO;
		double rejects = 0;

		if (givenElectricity > this.getMaxJoules())
		{
			rejects = givenElectricity - this.getRequest().getWatts();
		}

		this.onReceive(new ElectricityPack(givenElectricity / this.getVoltage(), this.getVoltage()));

		return (int) (rejects * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int getMaxSafeInput()
	{
		return 2048;
	}

	@Override
	public int getStored()
	{
		return (int) (this.getJoules() * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public void setStored(int energy)
	{
		this.setJoules(energy * UniversalElectricity.IC2_RATIO);
	}

	@Override
	public int addEnergy(int amount)
	{
		this.setJoules(this.getJoules() + (amount * UniversalElectricity.IC2_RATIO));
		return this.getStored();
	}

	@Override
	public int getCapacity()
	{
		return (int) (this.getMaxJoules() * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int getOutput()
	{
		return this.getMaxSafeInput();
	}

	@Override
	public boolean isTeleporterCompatible(Direction side)
	{
		return false;
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
		if (this.canConnect(from))
		{
			return (int) (this.getRequest().getWatts() * UniversalElectricity.TO_BC_RATIO);
		}

		return 0;
	}
}
