package universalelectricity.prefab.microblock;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockMicroblock extends BlockContainer
{
	public BlockMicroblock(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		/**
		 * Changes the metadata based on where the block is on.
		 */
		if (this.canPlaceMicroblock(world, x, y, z, side, hitX, hitY, hitZ))
		{
			return ForgeDirection.getOrientation(side).getOpposite().ordinal();
		}

		return metadata;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (this.canPlaceMicroblock(world, x, y, z, side, hitX, hitY, hitZ))
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

			if (tileEntity != null && tileEntity instanceof IMicroblock)
			{
				if (entityPlayer.getCurrentEquippedItem() != null)
					return ((IMicroblock) tileEntity).placeComponent(entityPlayer.getCurrentEquippedItem(), entityPlayer, side, hitX, hitY, hitZ);
			}
		}
		return false;
	}

	public boolean canPlaceMicroblock(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return true;
	}
}
