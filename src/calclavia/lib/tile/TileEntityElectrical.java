package calclavia.lib.tile;

import java.util.EnumSet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.item.ElectricItemHelper;

@UniversalClass
public class TileEntityElectrical extends TileEntityAdvanced implements IEnergyInterface, IEnergyContainer
{
	protected EnergyStorage energyStorage;

	/**
	 * Recharges electric item.
	 */
	public void recharge(ItemStack itemStack)
	{
		this.energyStorage.setEnergy(this.energyStorage.getEnergy() - ElectricItemHelper.chargeItem(itemStack, this.energyStorage.getEmptySpace()));
	}

	/**
	 * Discharges electric item.
	 */
	public void discharge(ItemStack itemStack)
	{
		this.energyStorage.setEnergy(this.energyStorage.getEnergy() + ElectricItemHelper.dischargeItem(itemStack, this.energyStorage.getEnergy()));
	}

	/**
	 * The electrical input direction.
	 * 
	 * @return The direction that electricity is entered into the tile. Return null for no input. By
	 * default you can accept power from all sides.
	 */
	public EnumSet<ForgeDirection> getInputDirections()
	{
		return EnumSet.allOf(ForgeDirection.class);
	}

	/**
	 * The electrical output direction.
	 * 
	 * @return The direction that electricity is output from the tile. Return null for no output. By
	 * default it will return an empty EnumSet.
	 */
	public EnumSet<ForgeDirection> getOutputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		if (direction == null || direction.equals(ForgeDirection.UNKNOWN))
		{
			return false;
		}

		return this.getInputDirections().contains(direction) || this.getOutputDirections().contains(direction);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.energyStorage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.energyStorage.writeToNBT(nbt);
	}

	@Override
	public long getEnergy(ForgeDirection from)
	{
		return this.energyStorage.getEnergy();
	}

	@Override
	public long getEnergyCapacity(ForgeDirection from)
	{
		return this.energyStorage.getEnergyCapacity();
	}

	@Override
	public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
	{
		return this.energyStorage.receiveEnergy(receive, doReceive);
	}

	@Override
	public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
	{
		return this.energyStorage.extractEnergy(extract, doExtract);
	}

	@Override
	public void setEnergy(ForgeDirection from, long energy)
	{
		this.energyStorage.setEnergy(energy);
	}
}