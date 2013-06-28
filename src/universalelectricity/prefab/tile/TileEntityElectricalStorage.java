package universalelectricity.prefab.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import universalelectricity.core.block.IEnergyStorage;

public abstract class TileEntityElectricalStorage extends TileEntityDisableable implements IEnergyStorage
{
	public float storedEnergy;

	@Override
	public void setEnergyStored(float energy)
	{
		this.storedEnergy = Math.max(Math.min(energy, this.getMaxEnergyStored()), 0);
	}

	@Override
	public float getEnergyStored()
	{
		return this.storedEnergy;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		storedEnergy = par1NBTTagCompound.getFloat("storedEnergy");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setFloat("storedEnergy", storedEnergy);
	}
}
