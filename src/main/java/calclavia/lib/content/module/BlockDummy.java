package calclavia.lib.content.module;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.prefab.vector.Cuboid;
import calclavia.lib.render.block.BlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDummy extends Block implements ITileEntityProvider
{
	/**
	 * A dummy instance of the block used to forward methods to.
	 */
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

		dummyTile.bounds.setBounds(this);

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

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		inject(world, x, y, z);
		getTile(world, x, y, z).collide(entity);
		eject(world, x, y, z);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
	{
		inject(world, x, y, z);

		Iterable<Cuboid> bounds = getTile(world, x, y, z).getCollisionBoxes(aabb != null ? new Cuboid(aabb).translate(new Vector3(x, y, z).invert()) : null, entity);

		if (bounds != null)
		{
			for (Cuboid cuboid : bounds)
				list.add(cuboid.clone().translate(new Vector3(x, y, z)).toAABB());
		}

		eject(world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		inject(world, x, y, z);
		Cuboid value = getTile(world, x, y, z).getSelectBounds().clone().translate(new Vector3(x, y, z));
		eject(world, x, y, z);
		return value.toAABB();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		inject(world, x, y, z);
		Cuboid value = getTile(world, x, y, z).getCollisionBounds().clone().translate(new Vector3(x, y, z));
		eject(world, x, y, z);
		return value.toAABB();
	}

	/**
	 * Injects and ejects data from the TileEntity.
	 */
	public void inject(World world, int x, int y, int z)
	{
		dummyTile.worldObj = world;
		dummyTile.xCoord = x;
		dummyTile.yCoord = y;
		dummyTile.zCoord = z;
	}

	public void eject(World world, int x, int y, int z)
	{
		dummyTile.worldObj = null;
		dummyTile.xCoord = 0;
		dummyTile.yCoord = 0;
		dummyTile.zCoord = 0;
	}

	public TileBlock getTile(World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof TileBlock)
		{
			return (TileBlock) tile;
		}

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
