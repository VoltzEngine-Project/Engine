package universalelectricity.prefab.conductor;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.microblock.BlockMicroblock;

public abstract class BlockConductor extends BlockMicroblock
{
	public float wireWidth = 2f / 16f;

	public BlockConductor(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));

		if (direction == ForgeDirection.UP)
		{
			return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + 0.5f - wireWidth, y + 1 - wireWidth, z + 0.5f - wireWidth, x + 0.5f + wireWidth, y + 1, z + 0.5f + wireWidth);
		}
		else if (direction == ForgeDirection.DOWN)
		{
			return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + 0.5f - wireWidth, y, z + 0.5f - wireWidth, x + 0.5f + wireWidth, y + wireWidth, z + 0.5f + wireWidth);
		}

		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List par6List, Entity entity)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
		AxisAlignedBB aabb = null;

		if (direction == ForgeDirection.UP)
		{
			aabb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + 0.5 - wireWidth, y + 1 - wireWidth, z + 0.5 - wireWidth, x + 0.5 + wireWidth, y + 1, z + 0.5 + wireWidth);
		}
		else if (direction == ForgeDirection.DOWN)
		{
			aabb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + 0.5 - wireWidth, y, z + 0.5 - wireWidth, x + 0.5 + wireWidth, y + wireWidth, z + 0.5 + wireWidth);
		}

		if (aabb != null && axisAlignedBB.intersectsWith(aabb))
		{
			par6List.add(aabb);
		}

	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));

		if (direction == ForgeDirection.UP)
		{
			setBlockBounds(0.5f - wireWidth, 1 - wireWidth, 0.5f - wireWidth, 0.5f + wireWidth, 1, 0.5f + wireWidth);
		}
		else if (direction == ForgeDirection.DOWN)
		{
			setBlockBounds(0.5f - wireWidth, 0, 0.5f - wireWidth, 0.5f + wireWidth, wireWidth, 0.5f + wireWidth);
		}
		else
		{
			super.setBlockBoundsBasedOnState(world, x, y, z);
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

	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		Vector3 position = new Vector3(x, y, z);
		position.modifyPositionFromSide(ForgeDirection.getOrientation(side).getOpposite());
		return world.isBlockNormalCube(position.intX(), position.intY(), position.intZ());
	}
}
