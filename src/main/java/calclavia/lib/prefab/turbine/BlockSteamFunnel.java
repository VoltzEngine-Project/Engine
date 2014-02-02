package calclavia.lib.prefab.turbine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import calclavia.lib.prefab.block.BlockTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Funnel block
 */
public abstract class BlockSteamFunnel extends BlockTile
{
	private Icon iconTop;

	public BlockSteamFunnel(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		return side == 1 || side == 0 ? iconTop : this.blockIcon;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		super.registerIcons(iconRegister);
		this.iconTop = iconRegister.registerIcon(this.getUnlocalizedName().replace("tile.", "") + "_top");
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileSteamFunnel();
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
