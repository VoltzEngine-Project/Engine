package calclavia.lib.content.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.prefab.item.ItemBlockTooltip;
import calclavia.lib.prefab.vector.Cuboid;
import calclavia.lib.utility.LanguageUtility;
import calclavia.lib.utility.WrenchUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * All blocks inherit this class.
 * 
 * Note that a lot of the variables will not exist except on the primary instance of the TileBlock,
 * hosted in BlockDummy.
 * 
 * @author Calclavia
 * 
 */
public abstract class TileBlock extends TileEntity
{
	protected final Material material;
	public Class<? extends ItemBlock> itemBlock = ItemBlockTooltip.class;

	/**
	 * The unique string ID of this block.
	 */
	public final String name;
	public String textureName;
	public CreativeTabs creativeTab = null;
	public boolean normalRender = true;
	public boolean isOpaqueCube = true;
	public Cuboid bounds = Cuboid.full();
	public Block block;

	public TileBlock(String newName, Material newMaterial)
	{
		name = newName;
		material = newMaterial;
		textureName = name;
	}

	public TileBlock(Material newMaterial)
	{
		name = LanguageUtility.decapitalizeFirst(getClass().getSimpleName().replaceFirst("Tile", ""));
		material = newMaterial;
		textureName = name;
	}

	/**
	 * Called after the block is registred. Use this to add recipes.
	 */
	public void onInstantiate()
	{

	}

	public World world()
	{
		return worldObj;
	}

	public int x()
	{
		assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
		return xCoord;
	}

	public int y()
	{
		assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
		return yCoord;
	}

	public int z()
	{
		assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
		return zCoord;
	}

	public Vector3 position()
	{
		assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
		return new Vector3(this);
	}

	protected Vector3 center()
	{
		assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
		return Vector3.fromCenter(this);
	}

	@Override
	public Block getBlockType()
	{
		Block b = super.getBlockType();

		if (tile() == null || b == null)
		{
			return block;
		}

		return b;
	}

	/**
	 * @return Return "this" if the block requires a TileEntity.
	 */
	public TileBlock tile()
	{
		return null;
	}

	/**
	 * @return The containing block.
	 */
	public Block block()
	{
		return Block.blocksList[blockID()];
	}

	public int blockID()
	{
		return world().getBlockId(x(), y(), z());
	}

	public int metdata()
	{
		return world().getBlockMetadata(x(), y(), z());
	}

	/**
	 * Drops
	 */
	public ArrayList<ItemStack> getDrops(int metadata, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(getBlockType(), 1, 0));
		return drops;
	}

	public void getSubBlocks(int id, CreativeTabs creativeTab, List list)
	{
		list.add(new ItemStack(id, 1, 0));
	}

	public ItemStack getPickBlock(MovingObjectPosition target)
	{
		return new ItemStack(getBlockType(), 1, 0);
	}

	public int getLightValue(IBlockAccess access)
	{
		return Block.lightValue[access.getBlockId(x(), y(), z())];
	}

	/**
	 * Block events
	 */
	public void click(EntityPlayer player)
	{

	}

	public boolean activate(EntityPlayer player, int side, Vector3 vector3)
	{
		/**
		 * Check if the player is holding a wrench. If so, call the wrench event.
		 */
		if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), x(), y(), z()))
		{
			if (configure(player, side, vector3))
			{
				WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), x(), y(), z());
				return true;
			}

			return false;
		}

		return use(player, side, vector3);
	}

	protected boolean use(EntityPlayer player, int side, Vector3 vector3)
	{
		return false;
	}

	protected boolean configure(EntityPlayer player, int side, Vector3 vector3)
	{
		return false;
	}

	protected void onAdded()
	{
		onWorldJoin();
	}

	protected void onWorldJoin()
	{
	}

	protected void onNeighborChanged()
	{
	}

	protected void notifyChange()
	{
		world().notifyBlocksOfNeighborChange(x(), y(), z(), blockID());
	}

	protected void markRender()
	{
		world().markBlockForRenderUpdate(x(), y(), z());
	}

	protected void markUpdate()
	{
		world().markBlockForUpdate(x(), y(), z());
	}

	protected void updateLight()
	{
		world().updateAllLightTypes(x(), y(), z());
	}

	protected void scheduelTick(int delay)
	{
		world().scheduleBlockUpdate(x(), y(), z(), blockID(), delay);
	}

	/**
	 * Called when an entity collides with this block.
	 */
	public void collide(Entity entity)
	{

	}

	/**
	 * Collision
	 * Note that all bounds done in the the tile is relative to the tile's position.
	 */
	public Iterable<Cuboid> getCollisionBoxes(Cuboid intersect, Entity entity)
	{
		List<Cuboid> boxes = new ArrayList<Cuboid>();

		for (Cuboid cuboid : getCollisionBoxes())
			if (intersect != null && cuboid.intersects(intersect))
				boxes.add(cuboid);

		return boxes;
	}

	public Iterable<Cuboid> getCollisionBoxes()
	{
		return Arrays.asList(new Cuboid[] { bounds });
	}

	public Cuboid getSelectBounds()
	{
		return bounds;
	}

	public Cuboid getCollisionBounds()
	{
		return bounds;
	}

	/**
	 * Rendering
	 */
	@SideOnly(Side.CLIENT)
	TileRender renderer;

	@SideOnly(Side.CLIENT)
	public final TileRender getRenderer()
	{
		if (renderer == null)
			renderer = renderer();

		return renderer;
	}

	@SideOnly(Side.CLIENT)
	protected TileRender renderer()
	{
		return null;
	}

	public boolean shouldSideBeRendered(IBlockAccess access, int side)
	{
		return side == 0 && this.bounds.min.y > 0.0D ? true : (side == 1 && this.bounds.max.y < 1.0D ? true : (side == 2 && this.bounds.min.z > 0.0D ? true : (side == 3 && this.bounds.max.z < 1.0D ? true : (side == 4 && this.bounds.min.x > 0.0D ? true : (side == 5 && this.bounds.max.x < 1.0D ? true : !access.isBlockOpaqueCube(x(), y(), z()))))));
	}

	public interface IComparatorInputOverride
	{
		public int getComparatorInputOverride(int side);
	}

	public void onFillRain()
	{

	}

}
