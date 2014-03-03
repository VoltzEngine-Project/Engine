package calclavia.lib.content.module;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
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
		if (intersect != null && bounds.intersects(intersect))
			return getCollisionBoxes();

		return null;
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

}
