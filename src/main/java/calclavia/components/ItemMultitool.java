package calclavia.components;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import buildcraft.api.tools.IToolWrench;
import calclavia.components.tool.ToolMode;
import calclavia.lib.utility.LanguageUtility;
import calclavia.lib.utility.nbt.NBTUtility;

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
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Mode: " + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (player.isSneaking() && !world.isRemote)
		{
			MovingObjectPosition objectMouseOver = player.rayTrace(5, 1);
			// TODO: There's an issue here with right clicking air.

			if (objectMouseOver == null || objectMouseOver.hitVec == null)
			{
				setMode(itemStack, (getMode(itemStack) + 1) % ToolMode.REGISTRY.size());

				player.addChatMessage("Set tool mode to: " + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));
				return itemStack;
			}
		}

		return ToolMode.REGISTRY.get(getMode(itemStack)).onItemRightClick(itemStack, world, player);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return ToolMode.REGISTRY.get(getMode(stack)).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return ToolMode.REGISTRY.get(getMode(stack)).onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
	{
		return true;
	}

	public int getMode(ItemStack itemStack)
	{
		return NBTUtility.getNBTTagCompound(itemStack).getInteger("mode");
	}

	public void setMode(ItemStack itemStack, int mode)
	{
		NBTUtility.getNBTTagCompound(itemStack).setInteger("mode", mode);
	}

}
