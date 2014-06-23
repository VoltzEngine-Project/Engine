package resonant.engine.content;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import resonant.engine.content.tool.ToolMode;
import resonant.lib.util.LanguageUtility;
import resonant.lib.util.nbt.NBTUtility;

import java.util.List;

public class ItemScrewdriver extends ItemBase implements IToolWrench
{
	public ItemScrewdriver(int id)
	{
		super("screwdriver", id);
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
		par3List.add(LanguageUtility.getLocal("toolmode.mode") + ": " + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));
		par3List.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.getLocal("item.resonant:screwdriver.tooltip"), 4));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		// TODO: Fix this.
		if (player.isSneaking())
		{
			setMode(itemStack, (getMode(itemStack) + 1) % ToolMode.REGISTRY.size());

			if (!world.isRemote)
			{
				player.addChatMessage(new ChatComponentText(LanguageUtility.getLocal("item.resonant:screwdriver.toolmode.set") + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName())));
			}
			return itemStack;
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
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
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
