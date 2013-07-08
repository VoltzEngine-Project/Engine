package universalelectricity.prefab.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.block.IElectricalStorage;
import universalelectricity.core.electricity.ElectricityPack;

public abstract class TileEntityElectrical extends TileEntityAdvanced implements IElectrical, IElectricalStorage
{
	/**
	 * Make sure this field is initiated.
	 */
	public ElectricityHandler electricityHandler;

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}

	@Override
	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
	{
		return this.electricityHandler.receiveElectricity(receive, doReceive);
	}

	@Override
	public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide)
	{
		return this.electricityHandler.provideElectricity(request, doProvide);
	}

	@Override
	public float getVoltage()
	{
		return 120;
	}

	@Override
	public void setEnergyStored(float energy)
	{
		this.electricityHandler.setEnergyStored(energy);
	}

	@Override
	public float getEnergyStored()
	{
		return this.electricityHandler.getEnergyStored();
	}

	@Override
	public float getMaxEnergyStored()
	{
		return this.electricityHandler.getMaxEnergyStored();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.electricityHandler.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.electricityHandler.writeToNBT(nbt);
	}

}
