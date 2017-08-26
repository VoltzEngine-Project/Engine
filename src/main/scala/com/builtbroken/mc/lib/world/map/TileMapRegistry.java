package com.builtbroken.mc.lib.world.map;

import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.multiblock.TileMulti;
import com.builtbroken.mc.lib.world.map.radar.RadarMap;
import com.builtbroken.mc.lib.world.map.radar.data.RadarObject;
import com.builtbroken.mc.lib.world.map.radar.data.RadarTile;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;
import java.util.List;

/**
 * Map based system for tracking Tiles in the world that should not be tracked using systems like radar. This is designed for
 * internal machine support for things like wireless power.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public final class TileMapRegistry
{
    /** Used only for event calls */
    public static final TileMapRegistry INSTANCE = new TileMapRegistry();
    //TODO add client side version for mini-map like systems
    //TODO add per machine tracking map that uses line of sight so hills can block it's view. (Visible Area Cache in other words)
    /** World id to radar map */
    private static final HashMap<Integer, RadarMap> WORLD_TO_MAP = new HashMap();

    /**
     * Adds an entity to the radar map
     *
     * @param tile - entity
     * @return true if added
     */
    public static boolean add(TileEntity tile)
    {
        if (tile != null && tile.getWorld() != null && !tile.getWorld().isRemote)
        {
            RadarMap map = getRadarMapForWorld(tile.getWorld());
            return map != null ? getRadarMapForWorld(tile.getWorld()).add(tile) : false;
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
        if (tile != null && tile.getWorld() != null)
        {
            RadarMap map = getRadarMapForWorld(tile.getWorld());
            return map != null ? getRadarMapForWorld(tile.getWorld()).remove(tile) : false;
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
            return getRadarMapForDim(world.provider.getDimension());
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
        if (event.getChunk().getWorld() != null && event.getChunk().getWorld().provider != null)
        {
            int dim = event.getChunk().getWorld().provider.getDimension();
            if (WORLD_TO_MAP.containsKey(dim))
            {
                getRadarMapForDim(dim).remove(event.getChunk());
            }
        }
    }

    @SubscribeEvent
    public void worldUpdateTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.provider != null && event.side == Side.SERVER && event.phase == TickEvent.Phase.END)
        {
            int dim = event.world.provider.getDimension();
            if (WORLD_TO_MAP.containsKey(dim))
            {
                RadarMap map = getRadarMapForDim(dim);
                if (map.chunk_to_entities.isEmpty())
                {
                    WORLD_TO_MAP.remove(dim);
                }
                else
                {
                    map.update();
                }
            }
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().provider != null && !event.getWorld().isRemote)
        {
            int dim = event.getWorld().provider.getDimension();
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
                        if (Engine.runningAsDev)
                        {
                            Engine.logger().info("Removed tile from TileMap. Tile = " + object, new RuntimeException());
                        }
                    }
                }
            }
        }
    }
}
