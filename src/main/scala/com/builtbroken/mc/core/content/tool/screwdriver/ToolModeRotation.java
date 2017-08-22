package com.builtbroken.mc.core.content.tool.screwdriver;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolModeRotation extends ToolMode
{
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState block = world.getBlockState(pos);

		if (block != null && block.getBlock().rotateBlock(world, pos, side))
		{
			//((ItemScrewdriver) stack.getItem()).wrenchUsed(player, pos);
			player.swingArm(hand);
			return true;
		}

		return false;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);

		//TODO add IC2 support

		return false;
	}

	@Override
	public String getName()
	{
		return "toolmode.rotation.name";
	}
}
