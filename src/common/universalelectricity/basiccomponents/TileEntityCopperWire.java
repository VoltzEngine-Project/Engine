package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.Packet;
import universalelectricity.network.PacketManager;
import universalelectricity.prefab.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor
{
	public static double RESISTANCE = 0.05;
	public static double MAX_AMPS = 1000;

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
		if(!this.worldObj.isRemote)
		{
			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, Block.fire.blockID);
		}
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(UELoader.CHANNEL, this);
    }
}
