package com.builtbroken.mc.lib.world;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.data.Direction;
import com.builtbroken.mc.imp.transform.rotation.Quaternion;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.helper.DummyPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class full of generic World related methods
 *
 * @author DarkGuardsman
 */
public class WorldUtility
{
    public static void rotateVectorFromDirection(Pos vec, Direction dir)
    {
        switch (dir)
        {
            default:
                break;
            case UP:
                vec.transform(new Quaternion(180, Pos.east));
                break;
            case NORTH:
                vec.transform(new Quaternion(90, Pos.west));
                break;
            case SOUTH:
                vec.transform(new Quaternion(90, Pos.east));
                break;
            case WEST:
                vec.transform(new Quaternion(90, Pos.north));
                break;
            case EAST:
                vec.transform(new Quaternion(90, Pos.south));
                break;
        }
    }

    public static int getAngleFromDirection(Direction dir)
    {
        switch (dir)
        {
            default:
                break;
            case NORTH:
                return 90;
            case SOUTH:
                return -90;
            case WEST:
                return -180;
            case EAST:
                return 0;
        }

        return 0;
    }

    @SuppressWarnings("incomplete-switch")
    public static Direction invertX(Direction dir)
    {
        switch (dir)
        {
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
        }

        return dir;
    }

    @SuppressWarnings("incomplete-switch")
    public static Direction invertY(Direction dir)
    {
        switch (dir)
        {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
        }

        return dir;
    }

    @SuppressWarnings("incomplete-switch")
    public static Direction invertZ(Direction dir)
    {
        switch (dir)
        {
            case WEST:
                return Direction.EAST;
            case EAST:
                return Direction.WEST;
        }

        return dir;
    }

    /**
     * Used to find all tileEntities sounding the location you will have to filter for selective
     * tileEntities
     *
     * @return an array of up to 6 tileEntities
     */
    public static TileEntity[] getSurroundingTileEntities(TileEntity ent)
    {
        return getSurroundingTileEntities(ent.getWorld(), ent.getPos());
    }

    public static TileEntity[] getSurroundingTileEntities(World world, Pos vec)
    {
        return getSurroundingTileEntities(world, vec.toBlockPos());
    }

    public static TileEntity[] getSurroundingTileEntities(World world, BlockPos pos)
    {
        TileEntity[] list = new TileEntity[6];
        for (Direction direction : Direction.DIRECTIONS)
        {
            list[direction.ordinal()] = world.getTileEntity(new BlockPos(pos.getX() + direction.offsetX, pos.getY() + direction.offsetY, pos.getZ() + direction.offsetZ));
        }
        return list;
    }

    /**
     * Used to find which of 4 Corners this block is in a group of blocks 0 = not a corner 1-4 = a
     * corner of some direction
     */
    public static int corner(TileEntity entity)
    {
        TileEntity[] en = getSurroundingTileEntities(entity.getWorld(), entity.getPos());
        TileEntity north = en[Direction.NORTH.ordinal()];
        TileEntity south = en[Direction.SOUTH.ordinal()];
        TileEntity east = en[Direction.EAST.ordinal()];
        TileEntity west = en[Direction.WEST.ordinal()];

        if (west != null && north != null && east == null && south == null)
        {
            return 3;
        }
        if (north != null && east != null && south == null && west == null)
        {
            return 4;
        }
        if (east != null && south != null && west == null && north == null)
        {
            return 1;
        }
        if (south != null && west != null && north == null && east == null)
        {
            return 2;
        }

        return 0;
    }

    /**
     * gets all EntityItems in a location using a start and end point
     */
    public static List<EntityItem> findAllItemsIn(World world, Pos start, Pos end)
    {
        return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(start.x(), start.y(), start.z(), end.x(), end.y(), end.z()));
    }

    public static List<EntityItem> getEntitiesInDirection(World world, Pos center, Direction dir)
    {
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(center.x() + dir.offsetX, center.y() + dir.offsetY, center.z() + dir.offsetZ, center.x() + dir.offsetX + 1, center.y() + dir.offsetY + 1, center.z() + dir.offsetZ + 1), null);
        return list.size() > 0 ? list : null;
    }

    /**
     * Gets all EntityItems in an area and sorts them by a list of itemStacks
     *
     * @param world        - world being worked in
     * @param start        - start point
     * @param end          - end point
     * @param disiredItems - list of item that are being looked for
     * @return a list of EntityItem that match the itemStacks desired
     */
    public static List<EntityItem> findSelectItems(World world, Pos start, Pos end, List<ItemStack> disiredItems)
    {
        List<EntityItem> entityItems = findAllItemsIn(world, start, end);
        return filterEntityItemsList(entityItems, disiredItems);
    }

    /**
     * filters an EntityItem List to a List of Items
     */
    public static List<EntityItem> filterEntityItemsList(List<EntityItem> entityItems, List<ItemStack> disiredItems)
    {
        List<EntityItem> newItemList = new ArrayList<>();
        for (ItemStack itemStack : disiredItems)
        {
            for (EntityItem entityItem : entityItems)
            {
                if (entityItem.getItem().isItemEqual(itemStack) && !newItemList.contains(entityItem))
                {
                    newItemList.add(entityItem);
                    break;
                }
            }
        }
        return newItemList;
    }

    /**
     * filters out EnittyItems from an Entity list
     */
    public static List<EntityItem> filterOutEntityItems(List<Entity> entities)
    {
        List<EntityItem> newEntityList = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (entity instanceof EntityItem)
            {
                newEntityList.add((EntityItem) entity);
            }

        }
        return newEntityList;
    }

    /**
     * filter a list of itemStack to another list of itemStacks
     *
     * @param totalItems   - full list of items being filtered
     * @param desiredItems - list the of item that are being filtered too
     * @return a list of item from the original that are wanted
     */
    public static List<ItemStack> filterItems(List<ItemStack> totalItems, List<ItemStack> desiredItems)
    {
        List<ItemStack> newItemList = new ArrayList<>();

        for (ItemStack entityItem : totalItems)
        {
            for (ItemStack itemStack : desiredItems)
            {
                if (entityItem.getItem() == itemStack.getItem() && entityItem.getItemDamage() == itemStack.getItemDamage() && !newItemList.contains(entityItem))
                {
                    newItemList.add(entityItem);
                    break;
                }
            }
        }
        return newItemList;
    }

    public static void replaceTileEntity(Class<? extends TileEntity> findTile, Class<? extends TileEntity> replaceTile)
    {
        try
        {
            Map<String, Class> nameToClassMap = ObfuscationReflectionHelper.getPrivateValue(TileEntity.class, null, "field_" + "70326_a", "nameToClassMap", "a");
            Map<Class, String> classToNameMap = ObfuscationReflectionHelper.getPrivateValue(TileEntity.class, null, "field_" + "70326_b", "classToNameMap", "b");

            String findTileID = classToNameMap.get(findTile);

            if (findTileID != null)
            {
                nameToClassMap.put(findTileID, replaceTile);
                classToNameMap.put(replaceTile, findTileID);
                classToNameMap.remove(findTile);
                Engine.logger().info("Replaced TileEntity: " + findTile);
            }
            else
            {
                Engine.logger().error("Failed to replace TileEntity: " + findTile);
            }
        }
        catch (Exception e)
        {
            Engine.logger().error("Failed to replace TileEntity: " + findTile);
            e.printStackTrace();
        }
    }

    public static List<ItemStack> getItemStackFromBlock(World world, BlockPos pos)
    {
        IBlockState block = world.getBlockState(pos);

        if (block == null)
        {
            return null;
        }

        if (block.getBlock().isAir(block, world, pos))
        {
            return null;
        }

        NonNullList<ItemStack> ret = NonNullList.create();
        block.getBlock().getDrops(ret, world, pos, block, 0);
        float dropChance = ForgeEventFactory.fireBlockHarvesting(ret, world, pos, block, 0, 1.0F, false, DummyPlayer.get(world));

        ArrayList<ItemStack> returnList = new ArrayList<>();
        for (ItemStack s : ret)
        {
            if (world.rand.nextFloat() <= dropChance)
            {
                returnList.add(s);
            }
        }

        return returnList;
    }
}
