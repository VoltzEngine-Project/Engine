package calclavia.components.tool;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.Event.Result;
import universalelectricity.api.vector.Vector3;
import calclavia.components.ItemScrewdriver;
import calclavia.lib.utility.inventory.InventoryUtility;

public class ToolModeRotation extends ToolMode
{
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int blockID = world.getBlockId(x, y, z);
		int blockMeta = world.getBlockMetadata(x, y, z);
		Block block = Block.blocksList[blockID];

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof IWrenchable && ((IWrenchable) tile).wrenchCanSetFacing(player, side))
		{
			ForgeDirection direction = ForgeDirection.getOrientation(side);
			short setSide = 0;

			if (player.isSneaking())
			{
				direction = direction.getOpposite();
			}
			setSide = (short) direction.ordinal();

			if (setSide != ((IWrenchable) tile).getFacing())
			{
				((IWrenchable) tile).setFacing(setSide);
			}
			else if (((IWrenchable) tile).wrenchCanRemove(player))
			{
				ItemStack output = ((IWrenchable) tile).getWrenchDrop(player);

				if (output != null)
				{
					world.setBlockToAir(x, y, z);
					InventoryUtility.dropItemStack(world, new Vector3(x, y, z), output);
				}
			}

			return true;
		}
		else if (block != null && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
		{
			((ItemScrewdriver) stack.getItem()).wrenchUsed(player, x, y, z);
			player.swingItem();
			return !world.isRemote;
		}

		return false;
	}

	@Override
	public String getName()
	{
		return "toolmode.rotation.name";
	}
}
