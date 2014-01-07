package calclavia.components;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import buildcraft.api.tools.IToolWrench;
import calclavia.components.event.WrenchEvent;

public class ItemWrench extends ItemBase implements IToolWrench
{
	public ItemWrench(int id)
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
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int blockID = world.getBlockId(x, y, z);
		int blockMeta = world.getBlockMetadata(x, y, z);
		Block block = Block.blocksList[blockID];
		WrenchEvent evt = new WrenchEvent(world, x, y, z, side, hitX, hitY, hitZ, block, blockMeta);
		MinecraftForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled())
		{
			if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
			{
				this.wrenchUsed(entityPlayer, x, y, z);
				return true;
			}
		}

		return evt.getResult() == Result.DENY ? true : false;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
	{
		return true;
	}
}
