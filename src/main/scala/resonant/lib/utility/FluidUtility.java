package resonant.lib.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import resonant.lib.type.Pair;
import resonant.lib.utility.inventory.AutoCraftingManager;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

/** Fluid interactions.
 * 
 * @author DarkCow, Calclavia */
public class FluidUtility
{
    public static List<Pair<Integer, Integer>> replacableBlockMeta = new ArrayList<Pair<Integer, Integer>>();
    public static List<Block> replacableBlocks = new ArrayList<Block>();
    public static List<Block> nonBlockDropList = new ArrayList<Block>();

    static
    {

        /* Adds default fluid replaceable blocks */
        replacableBlocks.add(Block.crops);
        replacableBlocks.add(Block.deadBush);
        nonBlockDropList.add(Block.deadBush);
        // TODO have waterlily raise and lower when automaticly filling or draining a block rather
        // than remove it
        replacableBlocks.add(Block.waterlily);
        replacableBlocks.add(Block.mushroomRed);
        replacableBlocks.add(Block.mushroomBrown);
        replacableBlocks.add(Block.netherStalk);
        replacableBlocks.add(Block.sapling);
        replacableBlocks.add(Block.melonStem);
        nonBlockDropList.add(Block.melonStem);
        replacableBlocks.add(Block.pumpkinStem);
        nonBlockDropList.add(Block.pumpkinStem);
        replacableBlocks.add(Block.tallGrass);
        replacableBlocks.add(Block.torchWood);
    }

    public static int getFluidAmountFromBlock(World world, Vector3 vector)
    {
        FluidStack fluidStack = getFluidStackFromBlock(world, vector);
        return fluidStack != null ? fluidStack.amount : 0;
    }

    public static FluidStack getFluidStackFromBlock(World world, Vector3 vector)
    {
        int id = vector.getBlockID(world);
        int meta = vector.getBlockMetadata(world);

        if (Block.blocksList[id] instanceof IFluidBlock)
        {
            IFluidBlock fluidBlock = ((IFluidBlock) Block.blocksList[id]);
            return new FluidStack(fluidBlock.getFluid(), (int) (FluidContainerRegistry.BUCKET_VOLUME * fluidBlock.getFilledPercentage(world, vector.intX(), vector.intY(), vector.intZ())));
        }
        else if (id == Block.waterStill.blockID || id == Block.waterMoving.blockID)
        {
            if (meta == 0)
                return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
        }
        else if (id == Block.lavaStill.blockID || id == Block.lavaMoving.blockID)
        {
            if (meta == 0)
                return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
        }

        return null;
    }

    public static FluidTankInfo[] getTankInfo(World world, Vector3 posiiton, ForgeDirection from)
    {
        TileEntity tile = posiiton.getTileEntity(world);

        if (tile instanceof IFluidHandler)
        {
            if (((IFluidHandler) tile).getTankInfo(from) != null)
                return ((IFluidHandler) tile).getTankInfo(from);
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
            return (double) amount / (double) capacity;

        return 0;
    }

    public static double getAveragePercentageFilledForSides(Class classMask, double defaultFill, World world, Vector3 position, ForgeDirection... sides)
    {
        if (defaultFill > 0.98)
            return 1.0;

        double fullness = defaultFill;
        int count = 1;

        for (ForgeDirection side : sides)
        {
            TileEntity tile = position.clone().translate(side).getTileEntity(world);

            if (tile != null && (classMask == null || classMask.isAssignableFrom(tile.getClass())))
            {
                FluidTankInfo[] info = getTankInfo(world, position.clone().translate(side), side);

                if (info.length > 0)
                {
                    fullness += getFilledPercentage(info);
                    count++;
                }
            }
        }

        return Math.max(0, Math.min(1, fullness / count));
    }

    /** Gets the block's fluid if it has one
     * 
     * @param world - world we are working in
     * @param vector - 3D location in world
     * @return @Fluid that the block is */
    public static Fluid getFluidFromBlock(World world, Vector3 vector)
    {
        return FluidUtility.getFluidFromBlockID(vector.getBlockID(world));
    }

    /** Gets a fluid from blockID */
    public static Fluid getFluidFromBlockID(int id)
    {
        if (Block.blocksList[id] instanceof IFluidBlock)
        {
            return ((IFluidBlock) Block.blocksList[id]).getFluid();
        }
        else if (id == Block.waterStill.blockID || id == Block.waterMoving.blockID)
        {
            return FluidRegistry.WATER;
        }
        else if (id == Block.lavaStill.blockID || id == Block.lavaMoving.blockID)
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
        return stack;
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

    /** Drains a block of fluid
     * 
     * @Note sets the block with a client update only. Doesn't tick the block allowing for better
     * placement of fluid that can flow infinitely
     * 
     * @param doDrain - do the action
     * @return FluidStack drained from the block */
    public static FluidStack drainBlock(World world, Vector3 position, boolean doDrain)
    {
        return drainBlock(world, position, doDrain, 3);
    }

    /** Drains a block of fluid
     * 
     * @param doDrain - do the action
     * @param update - block update flag to use
     * @return FluidStack drained from the block */
    public static FluidStack drainBlock(World world, Vector3 position, boolean doDrain, int update)
    {
        if (world == null || position == null)
        {
            return null;
        }

        int blockID = position.getBlockID(world);
        int meta = position.getBlockMetadata(world);
        Block block = Block.blocksList[blockID];
        if (block != null)
        {
            if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, position.intX(), position.intY(), position.intZ()))
            {
                return ((IFluidBlock) block).drain(world, position.intX(), position.intY(), position.intZ(), doDrain);
            }
            else if ((block.blockID == Block.waterStill.blockID || block.blockID == Block.waterMoving.blockID) && position.getBlockMetadata(world) == 0)
            {
                if (doDrain)
                {
                    Vector3 vec = position.clone().translate(ForgeDirection.UP);
                    if (vec.getBlockID(world) == Block.waterlily.blockID)
                    {
                        vec.setBlock(world, 0, 0, update);
                        position.setBlock(world, blockID, meta);
                    }
                    else
                    {
                        position.setBlock(world, 0, 0, update);
                    }
                }
                return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
            }
            else if ((block.blockID == Block.lavaStill.blockID || block.blockID == Block.lavaMoving.blockID) && position.getBlockMetadata(world) == 0)
            {
                if (doDrain)
                {
                    position.setBlock(world, 0, 0, update);
                }
                return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
            }
        }
        return null;
    }

    /** Checks to see if a non-fluid block is able to be filled with fluid */
    public static boolean isFillableBlock(World world, Vector3 node)
    {
        if (world == null || node == null)
        {
            return false;
        }

        int blockID = node.getBlockID(world);
        int meta = node.getBlockMetadata(world);
        Block block = Block.blocksList[blockID];
        if (drainBlock(world, node, false) != null)
        {
            return false;
        }
        else if (block == null || block.blockID == 0 || block.isAirBlock(world, node.intX(), node.intY(), node.intZ()))
        {
            return true;
        }
        else if (!(block instanceof IFluidBlock || block instanceof BlockFluid) && block.isBlockReplaceable(world, node.intX(), node.intY(), node.intZ()) || replacableBlockMeta.contains(new Pair<Integer, Integer>(blockID, meta)) || replacableBlocks.contains(block))
        {
            return true;
        }
        return false;
    }

    /** Checks to see if a fluid related block is able to be filled */
    public static boolean isFillableFluid(World world, Vector3 node)
    {
        if (world == null || node == null)
        {
            return false;
        }

        int blockID = node.getBlockID(world);
        int meta = node.getBlockMetadata(world);
        Block block = Block.blocksList[blockID];
        // TODO when added change this to call canFill and fill
        if (drainBlock(world, node, false) != null)
        {
            return false;
        }
        else if (block instanceof IFluidBlock || block instanceof BlockFluid)
        {
            return meta != 0;
        }
        return false;
    }

    /** Helper method to fill a location with a fluid
     * 
     * Note: This does not update the block to prevent the liquid from flowing
     * 
     * @return */
    public static int fillBlock(World world, Vector3 node, FluidStack stack, boolean doFill)
    {
        if ((isFillableBlock(world, node) || isFillableFluid(world, node)) && stack != null && stack.amount >= FluidContainerRegistry.BUCKET_VOLUME)
        {
            if (doFill)
            {
                int blockID = node.getBlockID(world);
                int meta = node.getBlockMetadata(world);
                Block block = Block.blocksList[blockID];
                Vector3 vec = node.clone().translate(ForgeDirection.UP);

                if (block != null)
                {
                    if (block.blockID == Block.waterlily.blockID && vec.getBlockID(world) == 0)
                    {
                        vec.setBlock(world, blockID, meta);
                    }
                    else if (block != null && replacableBlocks.contains(block) && !nonBlockDropList.contains(block))
                    {
                        block.dropBlockAsItem(world, node.intX(), node.intY(), node.intZ(), meta, 1);
                        block.breakBlock(world, node.intX(), node.intY(), node.intZ(), blockID, meta);
                    }
                }

                node.setBlock(world, stack.getFluid().getBlockID());
            }
            return FluidContainerRegistry.BUCKET_VOLUME;
        }
        return 0;
    }

    /** Fills all instances of IFluidHandler surrounding the origin
     * 
     * @param stack - FluidStack that will be filled into the tanks
     * @param doFill - Actually perform the action or simulate action
     * @param ignore - ForgeDirections to ignore
     * @return amount of fluid that was used from the stack */
    public static int fillTanksAllSides(World world, Vector3 origin, FluidStack stack, boolean doFill, ForgeDirection... ignore)
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

    /** Fills an instance of IFluidHandler in the given direction
     * 
     * @param stack - FluidStack to fill the tank will
     * @param doFill - Actually perform the action or simulate action
     * @param direction - direction to fill in from the origin
     * @return amount of fluid that was used from the stack */
    public static int fillTankSide(World world, Vector3 origin, FluidStack stack, boolean doFill, ForgeDirection direction)
    {
        TileEntity entity = origin.clone().translate(direction).getTileEntity(world);
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
                break;

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

    /** Does all the work needed to fill or drain an item of fluid when a player clicks on the block. */
    public static boolean playerActivatedFluidItem(World world, int x, int y, int z, EntityPlayer entityplayer, int side)
    {
        // TODO Add double click support similar to the crates in assembly line
        ItemStack current = entityplayer.inventory.getCurrentItem();

        if (current != null && world.getBlockTileEntity(x, y, z) instanceof IFluidHandler)
        {
            FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(current);
            IFluidHandler tank = (IFluidHandler) world.getBlockTileEntity(x, y, z);

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
                                InventoryUtility.dropItemStack(new VectorWorld(entityplayer), filled);
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
                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, AutoCraftingManager.consumeItem(current, 1));
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

    /** Drains an item of fluid and fills the tank with what was drained
     * 
     * @param consumeItem - should it consume the item. Used mainly for creative mode players. This
     * does effect the return of the method
     * @return Item stack that would be returned if the item was drain of its fluid. Water bucket ->
     * empty bucket */
    public static ItemStack drainItem(ItemStack stack, IFluidHandler tank, ForgeDirection side)
    {
        if (stack != null && tank != null)
        {
            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);
            if (liquid != null)
            {
                if (tank.fill(side, liquid, true) > 0)
                {
                    return stack.getItem().getContainerItemStack(stack);
                }
            }
        }
        return stack;
    }

    /** Fills an item with fluid from the tank
     * 
     * @param consumeItem - should it consume the item. Used mainly for creative mode players. This
     * does effect the return of the method
     * @return Item stack that would be returned if the item was filled with fluid. empty bucket ->
     * water bucket */
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

    /** Builds a list of fluidStacks from FluidTankInfo general taken from an instanceof
     * IFluidHandler */
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
