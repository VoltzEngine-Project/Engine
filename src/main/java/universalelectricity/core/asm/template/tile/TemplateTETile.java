package universalelectricity.core.asm.template.tile;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import cofh.api.energy.IEnergyHandler;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific
 * CompatibilityType.
 * 
 * @author Calclavia
 * 
 */
public abstract class TemplateTETile implements IEnergyHandler, IEnergyInterface
{
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		return (int) (StaticTileForwarder.onReceiveEnergy(this, from, (int) (maxReceive * CompatibilityType.THERMAL_EXPANSION.ratio), !simulate) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return (int) (StaticTileForwarder.onExtractEnergy(this, from, (int) (maxExtract * CompatibilityType.THERMAL_EXPANSION.ratio), !simulate) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return StaticTileForwarder.canConnect(this, from);
	}

	/**
	 * Returns the amount of energy currently stored.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		if (this instanceof IEnergyContainer)
		{
			return (int) (StaticTileForwarder.getElectricityStored((IEnergyContainer) this, from) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
		}

		return 0;
	}

	/**
	 * Returns the maximum amount of energy that can be stored.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		if (this instanceof IEnergyContainer)
		{
			return (int) (StaticTileForwarder.getMaxElectricity((IEnergyContainer) this, from) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
		}

		return 0;
	}
}
