package calclavia.components;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import universalelectricity.api.vector.Vector3;
import buildcraft.api.tools.IToolWrench;
import calclavia.components.event.MultitoolEvent;
import calclavia.lib.utility.InventoryUtility;

public class ItemMultitool extends ItemBase implements IToolWrench
{
	public ItemMultitool(int id)
	{
		super("multitool", id);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
	{

	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int blockID = world.getBlockId(x, y, z);
		int blockMeta = world.getBlockMetadata(x, y, z);
		Block block = Block.blocksList[blockID];
		MultitoolEvent evt = new MultitoolEvent(world, stack, player, x, y, z, side, hitX, hitY, hitZ, block, blockMeta);
		MinecraftForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled())
		{
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
				this.wrenchUsed(player, x, y, z);
				return true;
			}
		}

		return evt.hasResult() ? evt.getResult() == Result.DENY ? true : false : true;
	}

	@Override
	public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
	{
		return true;
	}
}
