package calclavia.lib.content.module;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.prefab.item.ItemBlockTooltip;
import calclavia.lib.utility.WrenchUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * All blocks inherit this class.
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

	public TileBlock(String newName, Material newMaterial)
	{
		name = newName;
		material = newMaterial;
		textureName = name;
	}

	public TileBlock(Material newMaterial)
	{
		name = getClass().getSimpleName().replaceFirst("Tile", "");
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

	public boolean use(EntityPlayer player, int side, Vector3 vector3)
	{
		return false;
	}

	public boolean configure(EntityPlayer player, int side, Vector3 vector3)
	{
		return false;
	}

	/**
	 * Rendering
	 */
	/**
	 * Render the static, unmoving faces of this part into the world renderer.
	 * The Tessellator is already drawing.
	 * 
	 * @param olm An optional light matrix to be used for rendering things with perfect MC blended
	 * lighting (eg microblocks). Only use this if you have to.
	 * @param pass The render pass, 1 or 0
	 * @return True if render was used.
	 */
	@SideOnly(Side.CLIENT)
	public boolean renderStatic(Vector3 position)
	{
		return false;
	}

	/**
	 * Render the dynamic, changing faces of this part and other gfx as in a TESR.
	 * The Tessellator will need to be started if it is to be used.
	 * 
	 * @param pos The position of this block space relative to the renderer, same as x, y, z passed
	 * to TESR.
	 * @param frame The partial interpolation frame value for animations between ticks
	 * @param pass The render pass, 1 or 0
	 */
	@SideOnly(Side.CLIENT)
	public void renderDynamic(Vector3 position, float frame)
	{
	}

	@SideOnly(Side.CLIENT)
	public void renderItem(ItemStack itemStack)
	{
	}
}
