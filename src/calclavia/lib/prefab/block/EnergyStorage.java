package calclavia.lib.prefab.block;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Used longernally for IEnergylongerface tiles.
 * 
 * @author Calclavia, King Lemming
 * 
 */
public class EnergyStorage
{
	protected long energy;
	protected long capacity;
	protected long maxReceive;
	protected long maxExtract;

	public EnergyStorage(long capacity)
	{

		this(capacity, capacity, capacity);
	}

	public EnergyStorage(long capacity, long maxTransfer)
	{

		this(capacity, maxTransfer, maxTransfer);
	}

	public EnergyStorage(long capacity, long maxReceive, long maxExtract)
	{

		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public EnergyStorage readFromNBT(NBTTagCompound nbt)
	{

		this.energy = nbt.getLong("energy");
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{

		nbt.setLong("energy", energy);
		return nbt;
	}

	public void setCapacity(long capacity)
	{

		this.capacity = capacity;

		if (energy > capacity)
		{
			energy = capacity;
		}
	}

	public void setMaxTransfer(long maxTransfer)
	{

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(long maxReceive)
	{

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(long maxExtract)
	{

		this.maxExtract = maxExtract;
	}

	public long getMaxReceive()
	{

		return maxReceive;
	}

	public long getMaxExtract()
	{

		return maxExtract;
	}

	/**
	 * This function is included to allow for server -> client sync. Do not call this externally to
	 * the containing Tile Entity, as not all IEnergyHandlers are
	 * guaranteed to have it.
	 * 
	 * @param energy
	 */
	public void setEnergyStored(long energy)
	{

		this.energy = energy;

		if (this.energy > capacity)
		{
			this.energy = capacity;
		}
		else if (this.energy < 0)
		{
			this.energy = 0;
		}
	}

	/**
	 * This function is included to allow the containing tile to directly and efficiently modify the
	 * energy contained in the EnergyStorage. Do not rely on this
	 * externally, as not all IEnergyHandlers are guaranteed to have it.
	 * 
	 * @param energy
	 */
	public void modifyEnergyStored(long energy)
	{

		this.energy += energy;

		if (this.energy > capacity)
		{
			this.energy = capacity;
		}
		else if (this.energy < 0)
		{
			this.energy = 0;
		}
	}

	public long receiveEnergy(long receive, boolean doReceive)
	{

		long energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (doReceive)
		{
			energy += energyReceived;
		}
		return energyReceived;
	}

	public long extractEnergy(long extract, boolean doExtract)
	{
		long energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (doExtract)
		{
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	public boolean checkReceive(long receive)
	{
		return this.receiveEnergy(receive, false) >= receive;
	}

	public boolean checkExtract(long extract)
	{
		return this.extractEnergy(extract, false) >= extract;
	}

	public boolean isFull()
	{
		return this.getEnergyStored() >= this.getMaxEnergyStored();
	}

	public long getEnergyStored()
	{
		return energy;
	}

	public long getMaxEnergyStored()
	{
		return capacity;
	}

}
