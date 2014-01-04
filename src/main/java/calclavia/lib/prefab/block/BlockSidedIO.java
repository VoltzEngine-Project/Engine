package calclavia.lib.prefab.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import calclavia.lib.prefab.tile.IIO;

/**
 * For blocks that can be set to input/output for their sides.
 * 
 * @author Calclavia
 * 
 */
public abstract class BlockSidedIO extends BlockAdvanced
{
	public BlockSidedIO(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof IIO)
		{
			((IIO) tile).setIO(ForgeDirection.getOrientation(side), ((IIO) tile).getIO(ForgeDirection.getOrientation(side)));
		}

		return true;
	}
}
