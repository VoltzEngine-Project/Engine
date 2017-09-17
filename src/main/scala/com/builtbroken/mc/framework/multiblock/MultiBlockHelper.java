package com.builtbroken.mc.framework.multiblock;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dark on 8/15/2015.
 */
public class MultiBlockHelper
{
    private static final Logger logger = LogManager.getLogger("VE-MultiBlockHelper");

    /**
     * Builds a multi block structure using data from the provided tile
     *
     * @param world - world
     * @param tile  - multiblock host
     */
    public static void buildMultiBlock(World world, IMultiTileHost tile)
    {
        buildMultiBlock(world, tile, false, false);
    }

    /**
     * Builds a multi block structure using data from the provided tile
     *
     * @param world    - world
     * @param tile     - multiblock host
     * @param validate - if true will check if a tile already exists at location rather than placing a new one
     */
    public static void buildMultiBlock(World world, IMultiTileHost tile, boolean validate)
    {
        buildMultiBlock(world, tile, validate, false);
    }

    /**
     * Builds a multi block structure using data from the provided tile
     *
     * @param world    - world
     * @param tile     - multiblock host
     * @param validate - if true will check if a tile already exists at location rather than placing a new one
     * @param offset   - offset the map position by the tile center
     */
    public static void buildMultiBlock(World world, IMultiTileHost tile, boolean validate, boolean offset)
    {
        //Rare edge case, should never happen
        if (world == null)
        {
            logger.error("MultiBlockHelper: buildMultiBlock was called with a null world by " + tile, new RuntimeException());
            return;
        }
        //Rare edge case, should never happen
        if (tile == null)
        {
            logger.error("MultiBlockHelper: buildMultiBlock was called with a null tile ", new RuntimeException());
            return;
        }
        //Multi-block should be registered but just in case a dev forgot
        if (Engine.multiBlock != null)
        {
            //Get layout of multi-block for it's current state
            Map<IPos3D, String> map = tile.getLayoutOfMultiBlock();
            //Ensure the map is not null or empty in case there is no structure to generate
            if (map != null && !map.isEmpty())
            {
                //Keep track of position just for traceability
                int i = 0;
                //Loop all blocks and start placement
                for (Map.Entry<IPos3D, String> entry : map.entrySet())
                {
                    IPos3D location = entry.getKey();
                    String type = entry.getValue();
                    String dataString = null;
                    if (location == null)
                    {
                        logger.error("MultiBlockHelper: location[" + i + "] is null, this is most likely in error in " + tile);
                        i++;
                        continue;
                    }

                    if (type == null)
                    {
                        logger.error("MultiBlockHelper: type[" + i + "] is null, this is most likely in error in " + tile);
                        i++;
                        continue;
                    }

                    if (type.isEmpty())
                    {
                        logger.error("MultiBlockHelper: type[" + i + "] is empty, this is most likely in error in " + tile);
                        i++;
                        continue;
                    }

                    if (type.contains("#"))
                    {
                        dataString = type.substring(type.indexOf("#") + 1, type.length());
                        type = type.substring(0, type.indexOf("#"));
                    }

                    EnumMultiblock enumType = EnumMultiblock.get(type);
                    if (enumType != null)
                    {
                        //Moves the position based on the location of the host
                        if (offset)
                        {
                            location = new Location(tile.world().unwrap(), tile.x(), tile.y(), tile.z()).add(location);
                        }
                        TileEntity ent = world.getTileEntity(location.xi(), location.yi(), location.zi());
                        if (!validate || ent == null || enumType.clazz != ent.getClass())
                        {
                            if (!world.setBlock(location.xi(), location.yi(), location.zi(), Engine.multiBlock, enumType.ordinal(), 3))
                            {
                                logger.error("MultiBlockHelper: type[" + i + ", " + type + "] error block was not placed ");
                            }
                            ent = world.getTileEntity(location.xi(), location.yi(), location.zi());
                        }

                        if (ent instanceof IMultiTile)
                        {
                            ((IMultiTile) ent).setHost(tile);
                            setData(dataString, (IMultiTile) ent);
                        }
                        else
                        {
                            logger.error("MultiBlockHelper: type[" + i + ", " + type + "] tile at location is not IMultiTile, " + ent);
                        }
                    }
                    else
                    {
                        logger.error("MultiBlockHelper: type[" + i + ", " + type + "] is not a invalid multi tile type, this is most likely an error in " + tile);
                    }
                    i++;
                }
            }
            else
            {
                logger.error("Tile[" + tile + "] didn't return a structure map");
            }
        }
        else
        {
            logger.error("MultiBlock was never registered, this is a critical error and can have negative effects on gameplay. " +
                    "Make sure the block was not disabled in the configs and contact support to ensure nothing is broken", new RuntimeException());
        }
    }

    /**
     * Called to check if the multi-block's structure can be generated
     *
     * @param world  - world to generate inside
     * @param tile   - tile to pull data from and check against
     * @param offset - true to offset structure map by center of tile
     * @return true if the structure can be generated
     */
    public static boolean canBuild(IBlockAccess world, IMultiTileHost tile, boolean offset)
    {
        if (world != null && tile != null && Engine.multiBlock != null)
        {
            //Get layout of multi-block for it's current state
            Map<IPos3D, String> map = tile.getLayoutOfMultiBlock();
            return canBuild(world, tile.xi(), tile.yi(), tile.zi(), tile, map, offset);
        }
        return false;
    }

    /**
     * Called to check if a multi-block structure can be generated at the location
     *
     * @param world  - world to generate inside
     * @param x      - position
     * @param y      - position
     * @param z      - position
     * @param tile   - can be null, host that will take control of the structure
     * @param map    - blocks to place
     * @param offset - true to offset placement map by center
     * @return true if the structure can be generated
     */
    public static boolean canBuild(IBlockAccess world, int x, int y, int z, IMultiTileHost tile, Map<IPos3D, String> map, boolean offset)
    {
        if (world != null && Engine.multiBlock != null)
        {
            //Ensure the map is not null or empty in case there is no structure to generate
            if (map != null && !map.isEmpty())
            {
                //Loop all blocks and start placement
                for (Map.Entry<IPos3D, String> entry : map.entrySet())
                {
                    IPos3D location = entry.getKey();
                    String type = entry.getValue();
                    String dataString = null;
                    if (location == null || type == null || type.isEmpty())
                    {
                        return false;
                    }

                    if (type.contains("#"))
                    {
                        dataString = type.substring(type.indexOf("#") + 1, type.length());
                        type = type.substring(0, type.indexOf("#"));
                    }

                    EnumMultiblock enumType = EnumMultiblock.get(type);
                    if (enumType != null)
                    {
                        //Moves the position based on the location of the host
                        if (offset)
                        {
                            location = new Pos(x, y, z).add(location);
                        }
                        Block block = world.getBlock(location.xi(), location.yi(), location.zi());
                        if (!block.isAir(world, location.xi(), location.yi(), location.zi()) && !block.isReplaceable(world, location.xi(), location.yi(), location.zi()))
                        {
                            return false;
                        }
                        else if (block == Engine.multiBlock)
                        {
                            TileEntity tileEntity = world.getTileEntity(location.xi(), location.yi(), location.zi());
                            if (tileEntity instanceof IMultiTile && ((IMultiTile) tileEntity).getHost() != null && ((IMultiTile) tileEntity).getHost() != tile)
                            {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void setData(String dataString, IMultiTile ent)
    {
        if (ent instanceof TileMulti && dataString != null && !dataString.isEmpty())
        {
            String[] data;
            if (dataString.contains("|"))
            {
                data = dataString.split("|");
            }
            else
            {
                data = new String[]{dataString};
            }
            for (String d : data)
            {
                if (d.contains("="))
                {
                    String lowerCase = d.toLowerCase();
                    String value = lowerCase.substring(lowerCase.indexOf("=") + 1, lowerCase.length());

                    if (lowerCase.startsWith("renderblock"))
                    {
                        if (value.equals("true"))
                        {
                            ((TileMulti) ent).shouldRenderBlock = true;
                        }
                    }
                    else if (lowerCase.startsWith("bounds"))
                    {
                        if (value.contains("{") && value.contains("}") && value.contains(","))
                        {
                            String[] values = value.split(",");
                            if (values.length == 6)
                            {
                                double[] ints = new double[6];
                                boolean failed = false;
                                for (int se = 0; se < 6; se++)
                                {
                                    try
                                    {
                                        ints[se] = Double.parseDouble(values[se].replace("f", ""));
                                    }
                                    catch (NumberFormatException e)
                                    {
                                        failed = true;
                                        break;
                                    }
                                }
                                if (!failed)
                                {
                                    if (((TileMulti) ent).getWorldObj().isRemote)
                                    {
                                        ((TileMulti) ent).overrideRenderBounds = new Cube(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
                                    }
                                    ((TileMulti) ent).collisionBounds = new Cube(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
                                }
                            }
                        }
                        else
                        {
                            logger.error("Tile[" + ent + "] failed to parse bounds data " + d + " as it missing '{', '}, or ',");
                        }
                    }
                    else
                    {
                        logger.error("Tile[" + ent + "] didn't parse " + d);
                    }
                }
                else
                {
                    logger.error("Tile[" + ent + "] failed to parse " + d + " as it doesn't contain '='");
                }
            }
        }
        else
        {
            if (!(ent instanceof TileMulti))
            {
                logger.error("Tile[" + ent + "] needs to be an instanceof TileMulti in order to set data");
            }
        }
    }

    @Deprecated
    public static void destroyMultiBlockStructure(IMultiTileHost host)
    {
        destroyMultiBlockStructure(host, true);
    }

    /**
     * Breaks down the multiblock structure linked to the host
     *
     * @param host    - host providing the layout of the structure
     * @param doDrops - attempt to drop blocks?
     */
    public static void destroyMultiBlockStructure(IMultiTileHost host, boolean doDrops)
    {
        destroyMultiBlockStructure(host, doDrops, false);
    }

    /**
     * Breaks down the multiblock stucture linked to the host
     *
     * @param host    - host providing the layour of the structure
     * @param doDrops - attempt to drop blocks?
     * @param offset  - offset the layout by the location of the host?
     */
    public static void destroyMultiBlockStructure(IMultiTileHost host, boolean doDrops, boolean offset)
    {
        destroyMultiBlockStructure(host, doDrops, offset, true);
    }

    /**
     * Breaks down the multiblock stucture linked to the host
     *
     * @param host     - host providing the layour of the structure
     * @param doDrops  - attempt to drop blocks?
     * @param offset   - offset the layout by the location of the host?
     * @param killHost - destroy the host block as well?
     */
    public static void destroyMultiBlockStructure(IMultiTileHost host, boolean doDrops, boolean offset, boolean killHost)
    {
        if (host != null)
        {
            HashMap<IPos3D, String> map = host.getLayoutOfMultiBlock();
            if (map != null && !map.isEmpty())
            {
                IWorldPosition center;

                if (host instanceof TileEntity)
                {
                    center = new Location((TileEntity) host);
                }
                else if (host instanceof IWorldPosition)
                {
                    center = (IWorldPosition) host;
                }
                else
                {
                    logger.error("MultiBlockHelper >> Tile[" + host + "]'s is not a TileEntity or IWorldPosition instance, thus we can not get a position to break down the structure.");
                    return;
                }

                for (Map.Entry<IPos3D, String> entry : map.entrySet())
                {
                    Pos pos = entry.getKey() instanceof Pos ? (Pos) entry.getKey() : new Pos(entry.getKey());
                    if (offset)
                    {
                        pos = pos.add(center);
                    }
                    TileEntity tile = pos.getTileEntity(center.oldWorld());
                    if (tile instanceof IMultiTile)
                    {
                        ((IMultiTile) tile).setHost(null);
                        pos.setBlockToAir(center.oldWorld());
                    }
                }
                if (doDrops)
                {
                    InventoryUtility.dropBlockAsItem(center, killHost);
                }
                else if (killHost)
                {
                    center.oldWorld().setBlockToAir(center.xi(), center.yi(), center.zi());
                }
            }
            else
            {
                logger.error("MultiBlockHelper >> Tile[" + host + "]'s structure map is empty");
            }
        }
    }

    /**
     * Runs a world update on all members of the structure
     *
     * @param world
     * @param host
     * @param offset - off set the location data by the center of the host
     */
    public static void updateStructure(World world, IMultiTileHost host, boolean offset)
    {
        //TODO junit test
        if (!(host instanceof TileEntity))
        {
            Engine.error("Tile host is not an instance of TileEntity");
        }
        if (world == null)
        {
            Engine.error("Tile host is not an instance of TileEntity");
        }


        HashMap<IPos3D, String> map = host.getLayoutOfMultiBlock();
        if (map != null && !map.isEmpty())
        {
            int x = ((TileEntity) host).xCoord;
            int y = ((TileEntity) host).yCoord;
            int z = ((TileEntity) host).zCoord;
            Pos center = new Pos(x, y, z);

            for (Map.Entry<IPos3D, String> entry : map.entrySet())
            {
                Location pos = new Location(world, entry.getKey());
                if (offset)
                {
                    pos = pos.add(center);
                }
                pos.markForUpdate();
            }
            center.markForUpdate(world);
        }
        else
        {
            logger.error("Tile[" + host + "]'s structure map is empty");
        }
    }

    /**
     * Gets all the chunk that the structure is located in. UNFINISHED
     *
     * @param world  - world the structure is located in
     * @param host   - host of the structure
     * @param offset - off set the location data by the center of the host
     * @return
     */
    public static List<Chunk> getChunks(World world, IMultiTileHost host, boolean offset)
    {
        //TODO junit test
        if (!(host instanceof TileEntity))
        {
            Engine.error("Tile host is not an instance of TileEntity");
        }
        HashMap<IPos3D, String> map = host.getLayoutOfMultiBlock();
        if (map != null && !map.isEmpty())
        {
            List<Chunk> chunks = new ArrayList<Chunk>();

            //TODO optimize as this is not the best way, in terms of CPU, to find all chunks
            for (Map.Entry<IPos3D, String> entry : map.entrySet())
            {
                Location pos = new Location(world, entry.getKey());
                if (offset)
                {
                    pos = pos.add(((TileEntity) host).xCoord, ((TileEntity) host).yCoord, ((TileEntity) host).zCoord);
                }
                if (!chunks.contains(pos.getChunk()))
                {
                    chunks.add(pos.getChunk());
                }
            }
            return chunks;
        }
        else
        {
            Engine.error("Tile host[" + host + "] did have a map");
        }
        return new ArrayList();
    }
}
