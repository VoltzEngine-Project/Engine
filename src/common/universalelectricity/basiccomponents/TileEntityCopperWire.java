package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor
{
	public static final double RESISTANCE = 0.03;
	public static final double MAX_AMPS = 10000;

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
	public void onConductorMelt()
	{
		/*
		 * This part is slightly bugged and causes concurrent modification errors.
		this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
		*/
	}
}
