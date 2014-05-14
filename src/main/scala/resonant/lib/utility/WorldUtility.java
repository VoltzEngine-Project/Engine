package resonant.lib.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import resonant.lib.References;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

/** Class full of generic World related methods
 * 
 * @author DarkGuardsman */
public class WorldUtility
{
    public static void rotateVectorFromDirection(Vector3 vec, ForgeDirection dir)
    {
        switch (dir)
        {
            default:
                break;
            case UP:
                vec.rotate(180, Vector3.EAST());
                break;
            case NORTH:
                vec.rotate(90, Vector3.WEST());
                break;
            case SOUTH:
                vec.rotate(90, Vector3.EAST());
                break;
            case WEST:
                vec.rotate(90, Vector3.NORTH());
                break;
            case EAST:
                vec.rotate(90, Vector3.SOUTH());
                break;
        }
    }

    public static int getAngleFromForgeDirection(ForgeDirection dir)
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
    public static ForgeDirection invertX(ForgeDirection dir)
    {
        switch (dir)
        {
            case NORTH:
                return ForgeDirection.SOUTH;
            case SOUTH:
                return ForgeDirection.NORTH;
        }

        return dir;
    }

    @SuppressWarnings("incomplete-switch")
    public static ForgeDirection invertY(ForgeDirection dir)
    {
        switch (dir)
        {
            case UP:
                return ForgeDirection.DOWN;
            case DOWN:
                return ForgeDirection.UP;
        }

        return dir;
    }

    @SuppressWarnings("incomplete-switch")
    public static ForgeDirection invertZ(ForgeDirection dir)
    {
        switch (dir)
        {
            case WEST:
                return ForgeDirection.EAST;
            case EAST:
                return ForgeDirection.WEST;
        }

        return dir;
    }

    /** Used to find all tileEntities sounding the location you will have to filter for selective
     * tileEntities
     * 
     * @param world - the world being searched threw
     * @param x
     * @param y
     * @param z
     * @return an array of up to 6 tileEntities */
    public static TileEntity[] getSurroundingTileEntities(TileEntity ent)
    {
        return getSurroundingTileEntities(ent.worldObj, ent.xCoord, ent.yCoord, ent.zCoord);
    }

    public static TileEntity[] getSurroundingTileEntities(World world, Vector3 vec)
    {
        return getSurroundingTileEntities(world, vec.intX(), vec.intY(), vec.intZ());
    }

    public static TileEntity[] getSurroundingTileEntities(World world, int x, int y, int z)
    {
        TileEntity[] list = new TileEntity[6];
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            list[direction.ordinal()] = world.getBlockTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        }
        return list;
    }

    /** Used to find which of 4 Corners this block is in a group of blocks 0 = not a corner 1-4 = a
     * corner of some direction */
    public static int corner(TileEntity entity)
    {
        TileEntity[] en = getSurroundingTileEntities(entity.worldObj, entity.xCoord, entity.yCoord, entity.zCoord);
        TileEntity north = en[ForgeDirection.NORTH.ordinal()];
        TileEntity south = en[ForgeDirection.SOUTH.ordinal()];
        TileEntity east = en[ForgeDirection.EAST.ordinal()];
        TileEntity west = en[ForgeDirection.WEST.ordinal()];

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

    /** gets all EntityItems in a location using a start and end point */
    public static List<EntityItem> findAllItemsIn(World world, Vector3 start, Vector3 end)
    {
        return world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(start.x, start.y, start.z, end.x, end.y, end.z));
    }

    public static List<EntityItem> getEntitiesInDirection(World world, Vector3 center, ForgeDirection dir)
    {
        List<EntityItem> list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(center.x + dir.offsetX, center.y + dir.offsetY, center.z + dir.offsetZ, center.x + dir.offsetX + 1, center.y + dir.offsetY + 1, center.z + dir.offsetZ + 1), IEntitySelector.selectAnything);
        return list.size() > 0 ? list : null;
    }

    /** Gets all EntityItems in an area and sorts them by a list of itemStacks
     * 
     * @param world - world being worked in
     * @param start - start point
     * @param end - end point
     * @param disiredItems - list of item that are being looked for
     * @return a list of EntityItem that match the itemStacks desired */
    public static List<EntityItem> findSelectItems(World world, Vector3 start, Vector3 end, List<ItemStack> disiredItems)
    {
        List<EntityItem> entityItems = findAllItemsIn(world, start, end);
        return filterEntityItemsList(entityItems, disiredItems);
    }

    /** filters an EntityItem List to a List of Items */
    public static List<EntityItem> filterEntityItemsList(List<EntityItem> entityItems, List<ItemStack> disiredItems)
    {
        List<EntityItem> newItemList = new ArrayList<EntityItem>();
        for (ItemStack itemStack : disiredItems)
        {
            for (EntityItem entityItem : entityItems)
            {
                if (entityItem.getEntityItem().isItemEqual(itemStack) && !newItemList.contains(entityItem))
                {
                    newItemList.add(entityItem);
                    break;
                }
            }
        }
        return newItemList;
    }

    /** filters out EnittyItems from an Entity list */
    public static List<EntityItem> filterOutEntityItems(List<Entity> entities)
    {
        List<EntityItem> newEntityList = new ArrayList<EntityItem>();

        for (Entity entity : entities)
        {
            if (entity instanceof EntityItem)
            {
                newEntityList.add((EntityItem) entity);
            }

        }
        return newEntityList;
    }

    /** filter a list of itemStack to another list of itemStacks
     * 
     * @param totalItems - full list of items being filtered
     * @param desiredItems - list the of item that are being filtered too
     * @return a list of item from the original that are wanted */
    public static List<ItemStack> filterItems(List<ItemStack> totalItems, List<ItemStack> desiredItems)
    {
        List<ItemStack> newItemList = new ArrayList<ItemStack>();

        for (ItemStack entityItem : totalItems)
        {
            for (ItemStack itemStack : desiredItems)
            {
                if (entityItem.itemID == itemStack.itemID && entityItem.getItemDamage() == itemStack.getItemDamage() && !newItemList.contains(entityItem))
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
                References.LOGGER.fine("Replaced TileEntity: " + findTile);
            }
            else
            {
                References.LOGGER.severe("Failed to replace TileEntity: " + findTile);
            }
        }
        catch (Exception e)
        {
            References.LOGGER.severe("Failed to replace TileEntity: " + findTile);
            e.printStackTrace();
        }
    }

    /** Checks based on a bitmap for sides if this side can be rendered.
     * 
     * @return True if so. */
    public static boolean isEnabledSide(byte sideMap, ForgeDirection direction)
    {
        return (sideMap & (1 << direction.ordinal())) != 0;
    }

    public static byte setEnableSide(byte sideMap, ForgeDirection direction, boolean doEnable)
    {
        if (doEnable)
        {
            sideMap = (byte) (sideMap | (1 << direction.ordinal()));
        }
        else
        {
            sideMap = (byte) (sideMap & ~(1 << direction.ordinal()));
        }

        return sideMap;
    }

    public static List<ItemStack> getItemStackFromBlock(World world, int i, int j, int k)
    {
        Block block = Block.blocksList[world.getBlockId(i, j, k)];

        if (block == null)
            return null;

        if (block.isAirBlock(world, i, j, k))
            return null;

        int meta = world.getBlockMetadata(i, j, k);

        ArrayList<ItemStack> dropsList = block.getBlockDropped(world, i, j, k, meta, 0);
        float dropChance = ForgeEventFactory.fireBlockHarvesting(dropsList, world, block, i, j, k, meta, 0, 1.0F, false, MachinePlayer.get(world));

        ArrayList<ItemStack> returnList = new ArrayList<ItemStack>();
        for (ItemStack s : dropsList)
        {
            if (world.rand.nextFloat() <= dropChance)
            {
                returnList.add(s);
            }
        }

        return returnList;
    }
}
