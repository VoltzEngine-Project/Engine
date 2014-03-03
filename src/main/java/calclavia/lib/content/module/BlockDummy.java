package calclavia.lib.content.module;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.render.block.BlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDummy extends Block implements ITileEntityProvider
{
	public final TileBlock dummyTile;

	public BlockDummy(int id, String modPrefix, CreativeTabs defaultTab, TileBlock dummyTile)
	{
		super(id, dummyTile.material);
		this.dummyTile = dummyTile;
		setUnlocalizedName(modPrefix + dummyTile.name);
		setTextureName(modPrefix + dummyTile.textureName);

		if (dummyTile.creativeTab != null)
			setCreativeTab(dummyTile.creativeTab);
		else
			setCreativeTab(defaultTab);

		/**
		 * Reinject opaqueCube data
		 */
		opaqueCubeLookup[id] = isOpaqueCube();
		lightOpacity[id] = isOpaqueCube() ? 255 : 0;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		inject(world, x, y, z);
		// TODO: Raytrace player's look position to determine the hit.
		getTile(world, x, y, z).click(player);
		eject(world, x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		inject(world, x, y, z);
		boolean value = getTile(world, x, y, z).activate(player, side, new Vector3(hitX, hitY, hitZ));
		eject(world, x, y, z);
		return value;
	}

	/**
	 * Injects and ejects data from the TileEntity.
	 */
	public void inject(World world, int x, int y, int z)
	{
		TileBlock tile = getTile(world, x, y, z);
		tile.worldObj = world;
		tile.xCoord = x;
		tile.yCoord = y;
		tile.zCoord = z;
	}

	public void eject(World world, int x, int y, int z)
	{
		TileBlock tile = getTile(world, x, y, z);
		tile.worldObj = null;
		tile.xCoord = 0;
		tile.yCoord = 0;
		tile.zCoord = 0;
	}

	public TileBlock getTile(World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof TileBlock)
			return (TileBlock) tile;

		return dummyTile;
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return dummyTile.tile() != null;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		try
		{
			return dummyTile.tile().getClass().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean isOpaqueCube()
	{
		if (dummyTile == null)
			return true;

		return dummyTile.isOpaqueCube;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return dummyTile.normalRender;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType()
	{
		return BlockRenderingHandler.ID;
	}

}
