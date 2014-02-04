package calclavia.lib.prefab.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.VectorWorld;
import calclavia.components.tool.ToolModeLink;
import calclavia.lib.utility.WrenchUtility;

/**
 * For blocks that can be set to input/output for their sides.
 * 
 * @author Calclavia
 * 
 */
public abstract class BlockLinkable extends BlockAdvanced
{
	public BlockLinkable(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

		if (WrenchUtility.isWrench(itemStack))
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if (tile instanceof ILinkable)
			{
				ILinkable linkable = (ILinkable) tile;

				if (linkable.onLink(entityPlayer, ToolModeLink.getLink(itemStack)))
				{
					ToolModeLink.clearLink(itemStack);
				}
				else
				{
					ToolModeLink.setLink(itemStack, new VectorWorld(world, x, y, z));
				}

				return true;
			}
		}

		return false;
	}
}
