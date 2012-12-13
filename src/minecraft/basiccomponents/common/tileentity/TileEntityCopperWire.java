package basiccomponents.common.tileentity;

import net.minecraft.block.Block;
import universalelectricity.prefab.tile.TileEntityConductor;
import basiccomponents.common.BCLoader;

public class TileEntityCopperWire extends TileEntityConductor
{
	public static double RESISTANCE = 0.05;
	public static double MAX_AMPS = 1000;

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
	public double getMaxAmps()
	{
		return MAX_AMPS;
	}

	@Override
	public void onOverCharge()
	{
		if (!this.worldObj.isRemote)
		{
			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
		}
	}
}
