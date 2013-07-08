package universalelectricity.prefab.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.block.IElectricalStorage;
import universalelectricity.core.electricity.ElectricityPack;

/**
 * An optional easy way for you to handle electrical storage without hassle.
 * 
 * @author Calclavia
 * 
 */
public class ElectricityHandler implements IElectricalStorage
{
	public IElectrical tileEntity;
	public float energyStored = 0;
	public float maxEnergyStored = 0;

	public ElectricityHandler(IElectrical tileEntity, float maxEnergyStored)
	{
		this.tileEntity = tileEntity;
		this.maxEnergyStored = maxEnergyStored;
	}

	public float receiveElectricity(ElectricityPack receive, boolean doReceive)
	{
		if (receive != null)
		{
			float newStoredEnergy = Math.min(this.getEnergyStored() + receive.getWatts(), this.getMaxEnergyStored());

			if (doReceive)
			{
				this.setEnergyStored(newStoredEnergy);
			}

			return Math.max(receive.getWatts() - newStoredEnergy, 0);
		}

		return 0;
	}

	public ElectricityPack provideElectricity(ElectricityPack request, boolean doProvide)
	{
		if (request != null)
		{
			float requestedEnergy = Math.min(request.getWatts(), this.energyStored);

			if (doProvide)
			{
				this.setEnergyStored(this.energyStored - requestedEnergy);
			}

			return ElectricityPack.getFromWatts(requestedEnergy, this.tileEntity.getVoltage());
		}

		return new ElectricityPack();
	}

	@Override
	public void setEnergyStored(float energy)
	{
		this.energyStored = Math.max(Math.min(energy, this.getMaxEnergyStored()), 0);
	}

	@Override
	public float getEnergyStored()
	{
		return this.energyStored;
	}

	@Override
	public float getMaxEnergyStored()
	{
		return this.maxEnergyStored;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		this.energyStored = nbt.getFloat("energyStored");
		this.maxEnergyStored = nbt.getFloat("maxEnergyStored");
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("energyStored", this.energyStored);
		nbt.setFloat("maxEnergyStored", this.maxEnergyStored);
	}
}
