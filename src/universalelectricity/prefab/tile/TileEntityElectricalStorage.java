package universalelectricity.prefab.tile;

import universalelectricity.core.block.IEnergyStorage;

public abstract class TileEntityElectricalStorage extends TileEntityDisableable implements IEnergyStorage
{
	public float energy;

	@Override
	public void setEnergyStored(float energy)
	{
		this.energy = Math.max(Math.min(energy, this.getMaxEnergyStored()), 0);
	}

	@Override
	public float getEnergyStored()
	{
		return this.energy;
	}
}
