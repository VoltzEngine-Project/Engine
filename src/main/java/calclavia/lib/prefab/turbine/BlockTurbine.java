package calclavia.lib.prefab.turbine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import calclavia.lib.prefab.block.BlockTile;

/*
 * Turbine block, extend this.
 */
public class BlockTurbine extends BlockTile
{
	public BlockTurbine(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileTurbine)
		{
			return ((TileTurbine) tileEntity).getMultiBlock().toggleConstruct();
		}

		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileTurbine)
		{
			((TileTurbine) tileEntity).getMultiBlock().deconstruct();
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
