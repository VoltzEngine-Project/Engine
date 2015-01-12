package com.builtbroken.mc.lib.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.prefab.inventory.AutoCraftingManager;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.transform.vector.Location;

import java.util.*;
import java.util.Map.Entry;

/**
 * Fluid interactions.
 *
 * @author DarkCow, Calclavia
 */
public class FluidUtility
{
	public static Set<Pair<Block, Integer>> replacableBlockMeta = new HashSet();
	public static Set<Block> replacableBlocks = new HashSet();
	public static Set<Block> nonBlockDropList = new HashSet();

	static
	{

        /* Adds default fluid replaceable blocks */
		replacableBlocks.add(Blocks.wheat);
		replacableBlocks.add(Blocks.deadbush);
		nonBlockDropList.add(Blocks.deadbush);
		// TODO have waterlily raise and lower when auto filling or draining a block rather
		// than remove it
		replacableBlocks.add(Blocks.waterlily);
		replacableBlocks.add(Blocks.red_mushroom);
		replacableBlocks.add(Blocks.brown_mushroom);
		replacableBlocks.add(Blocks.nether_wart);
		replacableBlocks.add(Blocks.sapling);
		replacableBlocks.add(Blocks.melon_stem);
		nonBlockDropList.add(Blocks.melon_stem);
		replacableBlocks.add(Blocks.pumpkin_stem);
		nonBlockDropList.add(Blocks.pumpkin_stem);
		replacableBlocks.add(Blocks.tallgrass);
		replacableBlocks.add(Blocks.torch);
	}

	public static int getFluidAmountFromBlock(World world, Pos vector)
	{
		FluidStack fluidStack = getFluidStackFromBlock(world, vector);
		return fluidStack != null ? fluidStack.amount : 0;
	}

	public static FluidStack getFluidStackFromBlock(World world, Pos vector)
	{
		Block block = vector.getBlock(world);
		int meta = vector.getBlockMetadata(world);

		if (block instanceof IFluidBlock)
		{
			IFluidBlock fluidBlock = ((IFluidBlock) block);
			return new FluidStack(fluidBlock.getFluid(), (int) (FluidContainerRegistry.BUCKET_VOLUME * fluidBlock.getFilledPercentage(world, vector.xi(), vector.yi(), vector.zi())));
		}
		else if (block == Blocks.water || block == Blocks.flowing_water)
		{
			if (meta == 0)
			{
				return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
			}
		}
		else if (block == Blocks.lava || block == Blocks.flowing_lava)
		{
			if (meta == 0)
			{
				return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
			}
		}

		return null;
	}

	public static FluidTankInfo[] getTankInfo(World world, Pos posiiton, ForgeDirection from)
	{
		TileEntity tile = posiiton.getTileEntity(world);

		if (tile instanceof IFluidHandler)
		{
			if (((IFluidHandler) tile).getTankInfo(from) != null)
			{
				return ((IFluidHandler) tile).getTankInfo(from);
			}
		}

		return new FluidTankInfo[0];
	}

	public static double getFilledPercentage(FluidTankInfo... info)
	{
		int amount = 0;
		int capacity = 0;

		for (FluidTankInfo tankInfo : info)
		{
			if (tankInfo != null && tankInfo.fluid != null)
			{
				amount += tankInfo.fluid.amount;
				capacity += tankInfo.capacity;
			}
		}

		if (capacity > 0)
		{
			return (double) amount / (double) capacity;
		}

		return 0;
	}

	public static double getAveragePercentageFilledForSides(Class classMask, double defaultFill, World world, Pos position, ForgeDirection... sides)
	{
		double fullness = defaultFill;
		int count = 1;

		for (ForgeDirection side : sides)
		{
			TileEntity tile = position.clone().add(side).getTileEntity(world);

			if (tile != null && (classMask == null || classMask.isAssignableFrom(tile.getClass())))
			{
				FluidTankInfo[] info = getTankInfo(world, position.clone().add(side), side);

				if (info.length > 0)
				{
					fullness += getFilledPercentage(info);
					count++;
				}
			}
		}

		return Math.max(0, Math.min(1, fullness / count));
	}

	/**
	 * Gets the block's fluid if it has one
	 *
	 * @param world  - world we are working in
	 * @param vector - 3D location in world
	 * @return @Fluid that the block is
	 */
	public static Fluid getFluidFromBlock(World world, Pos vector)
	{
		return FluidUtility.getFluidFromBlockID(vector.getBlock(world));
	}

	/**
	 * Gets a fluid from blockID
	 */
	public static Fluid getFluidFromBlockID(Block block)
	{
		if (block instanceof IFluidBlock)
		{
			return ((IFluidBlock) block).getFluid();
		}
		else if (block == Blocks.water || block == Blocks.flowing_water)
		{
			return FluidRegistry.WATER;
		}
		else if (block == Blocks.lava || block == Blocks.flowing_lava)
		{
			return FluidRegistry.LAVA;
		}

		return null;
	}

	public static FluidStack getStack(FluidStack stack, int amount)
	{
		if (stack != null)
		{
			FluidStack newStack = stack.copy();
			newStack.amount = amount;
			return newStack;
		}
		return null;
	}

	public static boolean matchExact(FluidStack stack, FluidStack stack2)
	{
		if (stack == null && stack2 == null)
		{
			return true;
		}
		else if (stack != null && stack.isFluidEqual(stack2))
		{
			return stack.amount == stack2.amount;
		}
		return false;
	}

	/**
	 * Drains a block of fluid
	 *
	 * @param doDrain - do the action
	 * @return FluidStack drained from the block
	 * @Note sets the block with a client update only. Doesn't tick the block allowing for better
	 * placement of fluid that can flow infinitely
	 */
	public static FluidStack drainBlock(World world, Pos position, boolean doDrain)
	{
		return drainBlock(world, position, doDrain, 3);
	}

	/**
	 * Drains a block of fluid
	 *
	 * @param doDrain - do the action
	 * @param update  - block update flag to use
	 * @return FluidStack drained from the block
	 */
	public static FluidStack drainBlock(World world, Pos position, boolean doDrain, int update)
	{
		if (world == null || position == null)
		{
			return null;
		}

		Block block = position.getBlock(world);
		int meta = position.getBlockMetadata(world);

		if (block != null)
		{
			if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, position.xi(), position.yi(), position.zi()))
			{
				return ((IFluidBlock) block).drain(world, position.xi(), position.yi(), position.zi(), doDrain);
			}
			else if ((block == Blocks.water || block == Blocks.flowing_water) && position.getBlockMetadata(world) == 0)
			{
				if (doDrain)
				{
					Pos vec = position.clone().add(ForgeDirection.UP);

					if (vec.getBlock(world) == Blocks.water)
					{
						vec.setBlock(world, Blocks.air, 0, update);
						position.setBlock(world, block, meta);
					}
					else
					{
						position.setBlock(world, Blocks.air, 0, update);
					}
				}
				return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
			}
			else if ((block == Blocks.lava || block == Blocks.flowing_lava) && position.getBlockMetadata(world) == 0)
			{
				if (doDrain)
				{
					position.setBlock(world, Blocks.air, 0, update);
				}
				return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
			}
		}
		return null;
	}

	/**
	 * Checks to see if a non-fluid block is able to be filled with fluid
	 */
	public static boolean isFillableBlock(World world, Pos node)
	{
		if (world == null || node == null)
		{
			return false;
		}

		Block block = node.getBlock(world);
		int meta = node.getBlockMetadata(world);

		if (drainBlock(world, node, false) != null)
		{
			return false;
		}
		else if (block.isAir(world, node.xi(), node.yi(), node.zi()))
		{
			return true;
		}
		else if (!(block instanceof IFluidBlock || block instanceof BlockLiquid) && block.isReplaceable(world, node.xi(), node.yi(), node.zi()) || replacableBlockMeta.contains(new Pair(block, meta)) || replacableBlocks.contains(block))
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if a fluid related block is able to be filled
	 */
	public static boolean isFillableFluid(World world, Pos node)
	{
		if (world == null || node == null)
		{
			return false;
		}

		Block block = node.getBlock(world);
		int meta = node.getBlockMetadata(world);

		// TODO when added change this to call canFill and fill
		if (drainBlock(world, node, false) != null)
		{
			return false;
		}
		else if (block instanceof IFluidBlock || block instanceof BlockLiquid)
		{
			return meta != 0;
		}
		return false;
	}

	/**
	 * Helper method to fill a location with a fluid
	 * <p/>
	 * Note: This does not update the block to prevent the liquid from flowing
	 *
	 * @return
	 */
	public static int fillBlock(World world, Pos node, FluidStack stack, boolean doFill)
	{
		if ((isFillableBlock(world, node) || isFillableFluid(world, node)) && stack != null && stack.amount >= FluidContainerRegistry.BUCKET_VOLUME)
		{
			if (doFill)
			{
				Block block = node.getBlock(world);
				int meta = node.getBlockMetadata(world);

				Pos vec = node.clone().add(ForgeDirection.UP);

				if (block != null)
				{
					if (block == Blocks.water && vec.getBlock(world).isAir(world, node.xi(), node.yi(), node.zi()))
					{
						vec.setBlock(world, block, meta);
					}
					else if (replacableBlocks.contains(block) && !nonBlockDropList.contains(block))
					{
						block.dropBlockAsItem(world, node.xi(), node.yi(), node.zi(), meta, 1);
						block.breakBlock(world, node.xi(), node.yi(), node.zi(), block, meta);
					}
				}

				node.setBlock(world, stack.getFluid().getBlock());
			}
			return FluidContainerRegistry.BUCKET_VOLUME;
		}
		return 0;
	}

	/**
	 * Fills all instances of IFluidHandler surrounding the origin
	 *
	 * @param stack  - FluidStack that will be filled into the tanks
	 * @param doFill - Actually perform the action or simulate action
	 * @param ignore - ForgeDirections to ignore
	 * @return amount of fluid that was used from the stack
	 */
	public static int fillTanksAllSides(World world, Pos origin, FluidStack stack, boolean doFill, ForgeDirection... ignore)
	{
		int filled = 0;
		FluidStack fillStack = stack != null ? stack.copy() : null;
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			if (fillStack == null || fillStack.amount <= 0)
			{
				return filled;
			}
			if (ignore != null)
			{
				for (int i = 0; i < ignore.length; i++)
				{
					if (direction == ignore[i])
					{
						continue;
					}
				}
			}
			filled += fillTankSide(world, origin, stack, doFill, direction);
			fillStack = getStack(stack, stack.amount - filled);

		}
		return filled;
	}

	/**
	 * Fills an instance of IFluidHandler in the given direction
	 *
	 * @param stack     - FluidStack to fill the tank will
	 * @param doFill    - Actually perform the action or simulate action
	 * @param direction - direction to fill in from the origin
	 * @return amount of fluid that was used from the stack
	 */
	public static int fillTankSide(World world, Pos origin, FluidStack stack, boolean doFill, ForgeDirection direction)
	{
		TileEntity entity = origin.clone().add(direction).getTileEntity(world);
		if (entity instanceof IFluidHandler && ((IFluidHandler) entity).canFill(direction.getOpposite(), stack.getFluid()))
		{
			return ((IFluidHandler) entity).fill(direction.getOpposite(), stack, doFill);
		}
		return 0;
	}

	public static int fillAllTanks(List<IFluidTank> tanks, FluidStack resource, boolean doFill)
	{
		int totalFilled = 0;
		FluidStack fill = resource.copy();

		for (IFluidTank tank : tanks)
		{
			if (fill.amount > 0)
			{
				int filled = tank.fill(fill, doFill);
				totalFilled += filled;
				fill.amount -= filled;
			}
			else
			{
				break;
			}
		}

		return totalFilled;
	}

	public static FluidStack drainAllTanks(List<IFluidTank> tanks, int amount, boolean doDrain)
	{
		FluidStack drain = null;

		for (IFluidTank tank : tanks)
		{
			if (drain != null && drain.amount >= amount)
			{
				break;
			}

			FluidStack drained = tank.drain(amount, false);

			if (drained != null)
			{
				if (drain == null)
				{
					drain = drained;
					tank.drain(amount, doDrain);
				}
				else if (drain.equals(drained))
				{
					drain.amount += drained.amount;
					tank.drain(amount, doDrain);
				}
			}
		}

		return drain;
	}

	/**
	 * Does all the work needed to fill or drain an item of fluid when a player clicks on the block.
	 */
	public static boolean playerActivatedFluidItem(World world, int x, int y, int z, EntityPlayer entityplayer, int side)
	{
		// TODO Add double click support similar to the crates in assembly line
		ItemStack current = entityplayer.inventory.getCurrentItem();

		if (current != null && world.getTileEntity(x, y, z) instanceof IFluidHandler)
		{
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(current);
			IFluidHandler tank = (IFluidHandler) world.getTileEntity(x, y, z);

			if (fluid != null)
			{
				if (tank.fill(ForgeDirection.getOrientation(side), fluid.copy(), false) == fluid.amount)
				{
					tank.fill(ForgeDirection.getOrientation(side), fluid.copy(), true);
					if (!entityplayer.capabilities.isCreativeMode)
					{
						InventoryUtility.consumeHeldItem(entityplayer);
					}
					return true;
				}
			}
			else
			{
				FluidStack available = tank.drain(ForgeDirection.getOrientation(side), Integer.MAX_VALUE, false);

				if (available != null)
				{
					ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

					fluid = FluidContainerRegistry.getFluidForFilledItem(filled);

					if (fluid != null)
					{
						if (!entityplayer.capabilities.isCreativeMode)
						{
							if (!entityplayer.inventory.addItemStackToInventory(filled))
							{
								return false;
							}
							else
							{
								InventoryUtility.dropItemStack(new Location(entityplayer), filled);
							}
						}
						tank.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
						return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean playerActivatedFluidItem(List<IFluidTank> tanks, EntityPlayer entityplayer, int side)
	{
		ItemStack current = entityplayer.inventory.getCurrentItem();

		if (current != null)
		{
			FluidStack resource = FluidContainerRegistry.getFluidForFilledItem(current);

			if (resource != null)
			{
				if (fillAllTanks(tanks, resource, false) >= resource.amount)
				{
					fillAllTanks(tanks, resource, true);

					if (!entityplayer.capabilities.isCreativeMode)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, AutoCraftingManager.consumeItem(current, 1));
					}
					return true;
				}
			}
			else
			{
				FluidStack available = drainAllTanks(tanks, Integer.MAX_VALUE, false);

				if (available != null)
				{
					ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

					resource = FluidContainerRegistry.getFluidForFilledItem(filled);

					if (resource != null)
					{
						if (!entityplayer.capabilities.isCreativeMode)
						{
							if (current.stackSize > 1)
							{
								if (!entityplayer.inventory.addItemStackToInventory(filled))
								{
									return false;
								}
								else
								{
									entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, AutoCraftingManager.consumeItem(current, 1));
								}
							}
							else
							{
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, AutoCraftingManager.consumeItem(current, 1));
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled);
							}
						}

						drainAllTanks(tanks, resource.amount, true);
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Drains an item of fluid and fills the tank with what was drained
	 *
	 * @param stack - should it consume the item. Used mainly for creative mode players. This
	 *              does effect the return of the method
	 * @return Item stack that would be returned if the item was drain of its fluid. Water bucket ->
	 * empty bucket
	 */
	public static ItemStack drainItem(ItemStack stack, IFluidHandler tank, ForgeDirection side)
	{
		if (stack != null && tank != null)
		{
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);
			if (liquid != null)
			{
				if (tank.fill(side, liquid, true) > 0)
				{
					return stack.getItem().getContainerItem(stack);
				}
			}
		}
		return stack;
	}

	/**
	 * Fills an item with fluid from the tank
	 *
	 * @param stack - should it consume the item. Used mainly for creative mode players. This
	 *              does effect the return of the method
	 * @return Item stack that would be returned if the item was filled with fluid. empty bucket ->
	 * water bucket
	 */
	public static ItemStack fillItem(ItemStack stack, IFluidHandler tank, ForgeDirection side)
	{
		if (stack != null && tank != null)
		{
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);
			FluidStack drainStack = tank.drain(side, Integer.MAX_VALUE, false);
			if (liquid == null && drainStack != null)
			{
				ItemStack liquidItem = FluidContainerRegistry.fillFluidContainer(drainStack, stack);
				if (tank.drain(side, FluidContainerRegistry.getFluidForFilledItem(liquidItem), true) != null)
				{
					return liquidItem;
				}
			}
		}
		return stack;
	}

	/**
	 * Builds a list of fluidStacks from FluidTankInfo general taken from an instanceof
	 * IFluidHandler
	 */
	public static List<FluidStack> getFluidList(FluidTankInfo... fluidTankInfos)
	{
		List<FluidStack> stackList = new ArrayList<FluidStack>();
		HashMap<FluidStack, Integer> map = new HashMap<FluidStack, Integer>();
		if (fluidTankInfos != null)
		{
			for (int i = 0; i < fluidTankInfos.length; i++)
			{
				FluidTankInfo info = fluidTankInfos[i];
				if (info != null && info.fluid != null)
				{
					FluidStack stack = info.fluid;
					if (map.containsKey(FluidUtility.getStack(stack, 0)))
					{
						map.put(FluidUtility.getStack(stack, 0), map.get(FluidUtility.getStack(stack, 0)) + stack.amount);
					}
					else
					{
						map.put(FluidUtility.getStack(stack, 0), stack.amount);
					}
				}
			}
			Iterator<Entry<FluidStack, Integer>> it = map.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<FluidStack, Integer> entry = it.next();
				stackList.add(FluidUtility.getStack(entry.getKey(), entry.getValue()));
			}
		}
		return stackList;

	}
}
