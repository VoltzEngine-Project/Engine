package basiccomponents.common.tileentity;

import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.tile.TileEntityConductor;
import basiccomponents.common.BCLoader;

public class TileEntityCopperWire extends TileEntityConductor
{
	/**
	 * Changed this if your mod wants to nerf Basic Component's copper wire.
	 */
	public static double RESISTANCE = 0.05;
	public static double MAX_AMPS = 200;

	public TileEntityCopperWire()
	{
		this.channel = BCLoader.CHANNEL;
	}

	@Override
	public double getResistance()
	{
		return RESISTANCE;
	}

	@Override
	public double getCurrentCapcity()
	{
		return MAX_AMPS;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.getNetwork() != null && this.ticks % 20 == 0)
		{
			if (this.getNetwork().getProduced().amperes > this.getCurrentCapcity())
			{
				if (!this.worldObj.isRemote)
				{
					this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
				}
			}
		}
	}
}
