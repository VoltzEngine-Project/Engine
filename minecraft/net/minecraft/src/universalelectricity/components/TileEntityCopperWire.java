package net.minecraft.src.universalelectricity.components;

import net.minecraft.src.Block;
import net.minecraft.src.universalelectricity.UETileEntityConductor;

public class TileEntityCopperWire extends UETileEntityConductor
{
	@Override
	public int getVolts()
	{
		return 120;
	}
	
	/**
	 * Called when the conductor's voltage becomes higher than it should be.
	 * @param volts - The amount of volts being forced into the conductor
	 * @param ticks - The amount of ticks that the conductor is receiving the overCharge
	 */
	@Override
	protected void overCharge(int volts, int ticks)
	{
		//If the voltage to high for this type of cable, burn.
		this.worldObj.spawnParticle("largesmoke", (double)this.xCoord + 0.5D, (double)this.yCoord + 0.7D, (double)this.zCoord + 0.5D, 0, 0, 0);
		
		if(ticks == 50)
		{
			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
		}
	}

	@Override
	public int getElectricityCapacity()
	{
		return 120;
	}
	
}
