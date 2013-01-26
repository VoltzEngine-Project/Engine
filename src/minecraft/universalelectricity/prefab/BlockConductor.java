package universalelectricity.prefab;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.implement.IConductor;

public abstract class BlockConductor extends BlockContainer
{
	public float wireWidth = 0.2f;

	public BlockConductor(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public int onBlockPlaced(World par1World, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		/**
		 * Changes the metadata based on where the block is on.
		 */
		return ForgeDirection.getOrientation(side).getOpposite().ordinal();
	}

	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
	{
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));

		if (direction == ForgeDirection.UP)
		{
			return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + wireWidth, y + 1 - wireWidth, z + wireWidth, x + 1 - wireWidth, y + 1, z + 1 - wireWidth);
		}
		else if (direction == ForgeDirection.DOWN)
		{
			return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + wireWidth, y, z + wireWidth, x + 1 - wireWidth, y + wireWidth, z + 1 - wireWidth);
		}
		else
		{
			return super.getSelectedBoundingBoxFromPool(world, x, y, z);
		}
	}

	@Override
	public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List par6List, Entity entity)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
		AxisAlignedBB aabb = null;

		if (direction == ForgeDirection.UP)
		{
			aabb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + wireWidth, y + wireWidth, z + wireWidth);
		}
		else if (direction == ForgeDirection.DOWN)
		{
			aabb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y + 1 - wireWidth, z, x + 1, y + 1, z + 1);
		}

		if (aabb != null && axisAlignedBB.intersectsWith(aabb))
		{
			par6List.add(aabb);
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			if (tileEntity instanceof IConductor)
			{
				((IConductor) tileEntity).refreshConnectedBlocks();
				world.markBlockForUpdate(x, y, z);
			}
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

		if (tileEntity != null)
		{
			if (tileEntity instanceof IConductor)
			{
				((IConductor) tileEntity).refreshConnectedBlocks();
				world.markBlockForUpdate(x, y, z);
			}
		}
	}
}
