package universalelectricity.prefab;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;

public abstract class BlockConductor extends BlockContainer
{
	public BlockConductor(int id, Material material)
	{
		super(id, material);
	}
/*
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		
	}*/

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IConductor)
		{
			((IConductor) tileEntity).refreshConnectedBlocks(true);
			world.markBlockForUpdate(x, y, z);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed
	 * (coordinates passed are their own) Args: x, y, z, neighbor blockID
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IConductor)
		{
			((IConductor) tileEntity).refreshConnectedBlocks(true);
			world.markBlockForUpdate(x, y, z);
		}
	}
}
