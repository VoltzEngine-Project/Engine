package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import universalelectricity.prefab.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor
{
	public static double RESISTANCE = 0.05;
	public static double MAX_AMPS = 500;

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
		this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
	}
}
