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
	 */
	@Override
	protected void overCharge(int volts)
	{
		//If the voltage is twice the normal voltage, explode.
		if(volts > this.getVolts())
		{
			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
			this.worldObj.spawnParticle("largesmoke", this.xCoord, this.yCoord, this.zCoord, 0, 0, 0);
        }
	}
}
