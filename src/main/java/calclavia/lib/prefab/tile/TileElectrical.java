package calclavia.lib.prefab.tile;

import java.util.EnumSet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.vector.Vector3;

@UniversalClass
public class TileElectrical extends TileAdvanced implements IEnergyInterface, IEnergyContainer, IIO
{
	public EnergyStorageHandler energy;

	/**
	 * Recharges electric item.
	 */
	public void recharge(ItemStack itemStack)
	{
		this.energy.extractEnergy(CompatibilityModule.chargeItem(itemStack, this.energy.getEmptySpace(), true), true);
	}

	/**
	 * Discharges electric item.
	 */
	public void discharge(ItemStack itemStack)
	{
		this.energy.receiveEnergy(CompatibilityModule.dischargeItem(itemStack, this.energy.getEnergy(), true), true);
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
		this.energy.readFromNBT(nbt);

		if (saveIOMap)
		{
			this.ioMap = nbt.getShort("ioMap");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.energy.writeToNBT(nbt);

		if (saveIOMap)
		{
			nbt.setShort("ioMap", this.ioMap);
		}
	}

	@Override
	public long getEnergy(ForgeDirection from)
	{
		return this.energy.getEnergy();
	}

	@Override
	public long getEnergyCapacity(ForgeDirection from)
	{
		return this.energy.getEnergyCapacity();
	}

	@Override
	public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
	{
		if (this.getInputDirections().contains(from))
		{
			return this.energy.receiveEnergy(receive, doReceive);
		}

		return 0;
	}

	@Override
	public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
	{
		if (this.getOutputDirections().contains(from))
		{
			return this.energy.extractEnergy(extract, doExtract);
		}

		return 0;
	}

	@Override
	public void setEnergy(ForgeDirection from, long energy)
	{
		this.energy.setEnergy(energy);
	}

	protected long produce(long outputEnergy)
	{
		long usedEnergy = 0;

		for (ForgeDirection direction : this.getOutputDirections())
		{
			if (outputEnergy > 0)
			{
				TileEntity tileEntity = new Vector3(this).modifyPositionFromSide(direction).getTileEntity(this.worldObj);

				if (tileEntity != null)
				{
					usedEnergy += CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), outputEnergy, true);
				}
			}
		}

		return usedEnergy;
	}

	protected long produce()
	{
		long totalUsed = 0;

		for (ForgeDirection direction : this.getOutputDirections())
		{
			if (this.energy.getEnergy() > 0)
			{
				TileEntity tileEntity = new Vector3(this).modifyPositionFromSide(direction).getTileEntity(this.worldObj);

				if (tileEntity != null)
				{
					long used = CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), this.energy.extractEnergy(this.energy.getEnergy(), false), true);
					this.energy.extractEnergy(used, true);
					totalUsed += used;
				}
			}
		}

		return totalUsed;
	}

	/**
	 * IO METHODS.
	 * Default: Connect from all sides. "111111"
	 * 0 - Nothing
	 * 1 - Input
	 * 2 - Output
	 */
	private short ioMap = 364;
	protected boolean saveIOMap = false;

	/**
	 * The electrical input direction.
	 * 
	 * @return The direction that electricity is entered into the tile. Return null for no input. By
	 * default you can accept power from all sides.
	 */
	@Override
	public EnumSet<ForgeDirection> getInputDirections()
	{
		EnumSet<ForgeDirection> dirs = EnumSet.noneOf(ForgeDirection.class);

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			if (getIO(direction) == 1)
			{
				dirs.add(direction);
			}
		}

		return dirs;
	}

	/**
	 * The electrical output direction.
	 * 
	 * @return The direction that electricity is output from the tile. Return null for no output. By
	 * default it will return an empty EnumSet.
	 */
	@Override
	public EnumSet<ForgeDirection> getOutputDirections()
	{
		EnumSet<ForgeDirection> dirs = EnumSet.noneOf(ForgeDirection.class);

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			if (getIO(direction) == 2)
			{
				dirs.add(direction);
			}
		}

		return dirs;
	}

	@Override
	public void setIO(ForgeDirection dir, int type)
	{
		String currentIO = Integer.toString(ioMap, 3);
		StringBuilder str = new StringBuilder(currentIO);
		str.setCharAt(dir.ordinal(), Integer.toString(type).charAt(0));
		this.ioMap = Short.parseShort(str.toString(), 3);
	}

	@Override
	public int getIO(ForgeDirection dir)
	{
		String currentIO = Integer.toString(ioMap, 3);
		return Integer.parseInt("" + currentIO.charAt(dir.ordinal()));
	}
}