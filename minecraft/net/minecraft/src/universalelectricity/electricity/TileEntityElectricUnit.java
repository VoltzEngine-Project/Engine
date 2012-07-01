package net.minecraft.src.universalelectricity.electricity;

import net.minecraft.src.TileEntity;

/**
 * An easier way to implement the methods with default values set.
 * @author Calclavia
 *
 */
public abstract class TileEntityElectricUnit extends TileEntity implements IElectricUnit
{
	protected int disabledTicks = 0;
	
	@Override
	public void onDisable(int duration)
	{
		this.disabledTicks = duration;
	}

	@Override
	public boolean isDisabled()
	{
		return this.disabledTicks > 0;
	}

	@Override
	public void onUpdate(float watts, float voltage, byte side)
	{
		if(this.disabledTicks > 0)
		{
			this.disabledTicks -= this.getTickInterval();
			return;
		}
	}

	@Override
	public boolean canReceiveElectricity(byte side)
	{
		return false;
	}

	@Override
	public float getVoltage()
	{
		return 120;
	}
	
	/**
	 * How many world ticks there should be before this tile entity gets ticked?
	 * E.x Returning "1" will make this tile entity tick every single tick.
	 * @return - The tick intervals. Returns 0 if you wish it to not tick at all.
	 */
	@Override
	public int getTickInterval()
	{
		return 1;
	}
	
    /**
     * Determines if this TileEntity requires update calls.
     * A UE TileEntity DOES NOT need update calls because the updates will be called
     * via the ElectricityManager reducing lag.
     * @return True if you want updateEntity() to be called, false if not
     */
	@Override
    public boolean canUpdate()
    {
        return false;
    }
}
