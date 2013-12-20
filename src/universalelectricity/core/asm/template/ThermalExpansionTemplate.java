package universalelectricity.core.asm.template;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.Compatibility;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import cofh.api.energy.IEnergyHandler;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class ThermalExpansionTemplate extends TileEntity implements IEnergyHandler, IEnergyInterface
{
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		return (int) (StaticForwarder.receiveElectricity(this, from, (int) (maxReceive * Compatibility.TE_RATIO), !simulate) * Compatibility.TO_TE_RATIO);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return (int) (StaticForwarder.extractElectricity(this, from, (int) (maxExtract * Compatibility.TE_RATIO), !simulate) * Compatibility.TO_TE_RATIO);
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return StaticForwarder.canConnect(this, from);
	}

	/**
	 * Returns the amount of energy currently stored.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		if (this instanceof IEnergyContainer)
		{
			return (int) (StaticForwarder.getElectricityStored((IEnergyContainer) this, from) * Compatibility.TO_TE_RATIO);
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
			return (int) (StaticForwarder.getMaxElectricity((IEnergyContainer) this, from) * Compatibility.TO_TE_RATIO);
		}

		return 0;
	}
}
