package com.builtbroken.mc.lib.world.map.tile;

import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.multiblock.TileMulti;
import com.builtbroken.mc.lib.world.map.radar.RadarMap;
import com.builtbroken.mc.lib.world.map.radar.data.RadarObject;
import com.builtbroken.mc.lib.world.map.radar.data.RadarTile;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Map based system for tracking Tiles in the world that should not be tracked using systems like radar. This is designed for
 * internal machine support for things like wireless power.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
@Deprecated //TODO replace with wrapper as chunks already store a map of position to tile, we can just use it instead
public final class TileMapRegistry
{
    /** Used only for event calls */
    public static final TileMapRegistry INSTANCE = new TileMapRegistry();

    /** Number of chunks to queue for scan per world per tick */
    public static int CHUNKS_TO_SCAN_PER_TICK = 10;
    public static long CHUNKS_SCAN_TIME_LIMIT_NS = TimeUnit.NANOSECONDS.convert(10, TimeUnit.MILLISECONDS);

    /** World id to radar map */
    private static final HashMap<Integer, RadarMap> WORLD_TO_MAP = new HashMap();

    private static final HashMap<Integer, Queue<ChunkCoordIntPair>> CHUNKS_TO_SCAN = new HashMap();

    /**
     * Adds an entity to the radar map
     *
     * @param tile - entity
     * @return true if added
     */
    public static boolean add(TileEntity tile)
    {
        if (tile != null && tile.getWorldObj() != null && !tile.getWorldObj().isRemote)
        {
            RadarMap map = getRadarMapForWorld(tile.getWorldObj());
            return map != null ? getRadarMapForWorld(tile.getWorldObj()).add(tile) : false;
        }
        return false;
    }

    /**
     * Removes an entity from the radar map
     *
     * @param tile - entity
     * @return true if removed
     */
    public static boolean remove(TileEntity tile)
    {
        if (tile != null && tile.getWorldObj() != null)
        {
            RadarMap map = getRadarMapForWorld(tile.getWorldObj());
            return map != null ? getRadarMapForWorld(tile.getWorldObj()).remove(tile) : false;
        }
        return false;
    }

    /**
     * Gets a radar map for the world
     *
     * @param world - should be a valid world that is loaded and has a dim id
     * @return existing map, or new map if one does not exist
     */
    public static RadarMap getRadarMapForWorld(World world)
    {
        if (world != null && world.provider != null)
        {
            if (world.isRemote)
            {
                if (Engine.runningAsDev)
                {
                    Engine.logger().error("RadarRegistry: Radar data can not be requested client side.", new RuntimeException());
                }
                return null;
            }
            return getRadarMapForDim(world.provider.dimensionId);
        }
        //Only throw an error in dev mode, ignore in normal runtime
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("RadarRegistry: World can not be null or have a null provider when requesting a radar map", new RuntimeException());
        }
        return null;
    }

    /**
     * Gets a radar map for a dimension
     *
     * @param dimID - unique dim id
     * @return existing mpa, or new map if one does not exist
     */
    public static RadarMap getRadarMapForDim(int dimID)
    {
        if (!WORLD_TO_MAP.containsKey(dimID))
        {
            RadarMap map = new RadarMap(dimID);
            WORLD_TO_MAP.put(dimID, map);
            return map;
        }
        return WORLD_TO_MAP.get(dimID);
    }

    @SubscribeEvent
    public void chunkUnload(ChunkEvent.Unload event)
    {
        if (event.getChunk().worldObj != null && event.getChunk().worldObj.provider != null)
        {
            int dim = event.getChunk().worldObj.provider.dimensionId;
            if (WORLD_TO_MAP.containsKey(dim))
            {
                getRadarMapForDim(dim).remove(event.getChunk());
            }
        }
    }

    @SubscribeEvent
    public void chunkLoad(ChunkEvent.Load event)
    {
        final Chunk chunk = event.getChunk();
        final World world = chunk.worldObj;
        if (world != null && world.provider != null)
        {
            int dim = world.provider.dimensionId;
            if (!CHUNKS_TO_SCAN.containsKey(dim))
            {
                CHUNKS_TO_SCAN.put(dim, new LinkedList());
            }
            CHUNKS_TO_SCAN.get(dim).offer(chunk.getChunkCoordIntPair());
        }
    }

    @SubscribeEvent
    public void worldUpdateTick(TickEvent.WorldTickEvent event)
    {
        final World world = event.world;
        if (world.provider != null && event.side == Side.SERVER && event.phase == TickEvent.Phase.END)
        {
            int dim = world.provider.dimensionId;
            if (WORLD_TO_MAP.containsKey(dim))
            {
                //Update radar map
                RadarMap map = getRadarMapForDim(dim);
                if (map.chunk_to_entities.isEmpty())
                {
                    WORLD_TO_MAP.remove(dim);
                }
                else
                {
                    map.update();
                }

                //Scan chunk to load non-VE tiles into map
                if (CHUNKS_TO_SCAN.containsKey(dim))
                {
                    //Get queue for the world
                    Queue<ChunkCoordIntPair> queue = CHUNKS_TO_SCAN.get(dim);
                    if (!queue.isEmpty())
                    {
                        //Limit chunks scanned
                        int count = 0;

                        //Limit time spent per scan
                        long start = System.nanoTime();

                        //loop until we hit a stop condition
                        while (queue.peek() != null && count < CHUNKS_TO_SCAN_PER_TICK && (System.nanoTime() - start) < CHUNKS_SCAN_TIME_LIMIT_NS)
                        {
                            //Get next chunk
                            ChunkCoordIntPair position = queue.poll();

                            //Make sure chunk exists
                            if (world.getChunkProvider().chunkExists(position.chunkXPos, position.chunkZPos))
                            {
                                //Get chunk
                                Chunk chunk = world.getChunkFromChunkCoords(position.chunkXPos, position.chunkZPos);
                                if (chunk != null)
                                {
                                    //Iterate over convenient chunk map
                                    Map<ChunkPosition, TileEntity> tiles = chunk.chunkTileEntityMap;
                                    for (TileEntity tile : tiles.values())
                                    {
                                        if (!tile.isInvalid())
                                        {
                                            add(tile);
                                        }
                                    }
                                }
                                else
                                {
                                    //TODO error?
                                }
                            }
                            else if (Engine.runningAsDev)
                            {
                                Engine.logger().warn("TileMapRegistry#worldUpdateTick() >> Dim["
                                        + dim
                                        + "] >> Chunk" + position
                                        + " was queue for scan but did not exist when polled for scan" +
                                        "\nThis is likely a problem with map unloading or loading too often.");
                            }
                        }
                        //TODO even though scan time is low, might want to move it to a worker thread to off load CPU time on main thread
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event)
    {
        if (event.world.provider != null && !event.world.isRemote)
        {
            int dim = event.world.provider.dimensionId;
            if (WORLD_TO_MAP.containsKey(dim))
            {
                getRadarMapForDim(dim).unloadAll();
                WORLD_TO_MAP.remove(dim);
            }
        }
    }

    @SubscribeEvent
    public void onTileLoaded(TileEvent.TileLoadEvent event)
    {
        if (event.world.provider != null && !event.world.isRemote)
        {
            //Remove previous tiles
            removeTilesAtLocation(event.world, event.x, event.y, event.z);

            //Add new tile
            if (event.tile() != null)
            {
                //Exclude multi-tiles to prevent repeat entries
                if (!(event.tile() instanceof TileMulti))
                {
                    boolean added = add(event.tile());
                    if (Engine.runningAsDev)
                    {
                        if (added)
                        {
                            Engine.logger().info("Added tile to TileMap. Tile = " + event.tile());
                        }
                        else
                        {
                            Engine.logger().info("Failed to add tile to TileMap. Tile = " + event.tile());
                        }
                    }
                }
                else
                {
                    //TODO get host from multi-tile
                }
            }
            else if (Engine.runningAsDev)
            {
                Engine.logger().info("Error something tried to add a null tile to the map", new RuntimeException());
            }
        }
    }

    @SubscribeEvent
    public void onTileLoaded(TileEvent.TileUnLoadEvent event)
    {
        if (event.world.provider != null && !event.world.isRemote)
        {
            removeTilesAtLocation(event.world, event.x, event.y, event.z);
        }
    }

    /**
     * Called to remove tiles at the location
     * <p>
     * Will also clear any invalid tiles it happens to find as well.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public void removeTilesAtLocation(World world, int x, int y, int z)
    {
        RadarMap map = getRadarMapForWorld(world);
        if (map != null)
        {
            List<RadarObject> list = map.getRadarObjects(x, z, 5);
            for (RadarObject object : list)
            {
                if (object instanceof RadarTile)
                {
                    if (((RadarTile) object).tile.isInvalid() || object.xi() == x && object.yi() == y && object.zi() == z)
                    {
                        map.remove(object);
                    }
                }
            }
        }
    }
}
