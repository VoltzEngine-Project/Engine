/**
 * 
 */
package calclavia.components.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.VectorWorld;
import calclavia.lib.prefab.block.ILinkable;
import calclavia.lib.utility.nbt.NBTUtility;

/**
 * @author Calclavia
 */
public class ToolModeLink extends ToolMode
{
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		/*
		 * if (ControlKeyModifer.isControlDown(player))
		 * {
		 * if (tile instanceof ILinkable)
		 * {
		 * if (!world.isRemote)
		 * {
		 * if (((ILinkable) tile).onLink(player, this.getLink(stack)))
		 * {
		 * clearLink(stack);
		 * player.addChatMessage("Link cleared.");
		 * return true;
		 * }
		 * }
		 * }
		 * }
		 */

		if (!world.isRemote)
		{
			player.addChatMessage("Set link to block [" + x + ", " + y + ", " + z + "], Dimension: '" + world.provider.getDimensionName() + "'");
			setLink(stack, new VectorWorld(world, x, y, z));

			if (tile instanceof ILinkable)
			{
				if (!world.isRemote)
				{
					((ILinkable) tile).onLink(player, this.getLink(stack));
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public String getName()
	{
		return "toolmode.link.name";
	}

	public static boolean hasLink(ItemStack itemStack)
	{
		return getLink(itemStack) != null;
	}

	public static VectorWorld getLink(ItemStack itemStack)
	{
		if (itemStack.stackTagCompound == null || !itemStack.getTagCompound().hasKey("link"))
		{
			return null;
		}

		return new VectorWorld(itemStack.getTagCompound().getCompoundTag("link"));
	}

	public static void setLink(ItemStack itemStack, VectorWorld vec)
	{
		NBTUtility.getNBTTagCompound(itemStack).setCompoundTag("link", vec.writeToNBT(new NBTTagCompound()));
	}

	public static void clearLink(ItemStack itemStack)
	{
		itemStack.getTagCompound().removeTag("link");
	}

	public static void setSide(ItemStack itemStack, byte side)
	{
		NBTUtility.getNBTTagCompound(itemStack).setByte("linkSide", side);
	}

	public static byte getSide(ItemStack itemStack)
	{
		return NBTUtility.getNBTTagCompound(itemStack).getByte("linkSide");
	}
}
