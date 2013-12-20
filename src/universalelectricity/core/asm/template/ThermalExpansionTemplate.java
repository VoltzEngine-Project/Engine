package universalelectricity.core.asm.template;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.IElectricalStorage;
import universalelectricity.api.IElectricityHandler;
import universalelectricity.compatibility.Compatibility;
import cofh.api.energy.IEnergyHandler;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class ThermalExpansionTemplate extends TileEntity implements IEnergyHandler, IElectricityHandler
{
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		return (int) (this.receiveElectricity(from, (int) (maxReceive * Compatibility.TE_RATIO), !simulate) * Compatibility.TO_TE_RATIO);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return (int) (this.extractElectricity(from, (int) (maxExtract * Compatibility.TE_RATIO), !simulate) * Compatibility.TO_TE_RATIO);
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return this.canConnect(from);
	}

	/**
	 * Returns the amount of energy currently stored.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		if (this instanceof IElectricalStorage)
		{
			return (int) (((IElectricalStorage) this).getEnergyStored(from) * Compatibility.TO_TE_RATIO);
		}

		return 0;
	}

	/**
	 * Returns the maximum amount of energy that can be stored.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		if (this instanceof IElectricalStorage)
		{
			return (int) (((IElectricalStorage) this).getMaxEnergyStored(from) * Compatibility.TO_TE_RATIO);
		}

		return 0;
	}
}
