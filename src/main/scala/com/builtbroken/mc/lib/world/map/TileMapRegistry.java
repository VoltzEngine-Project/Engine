package com.builtbroken.mc.lib.world.map;

import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.world.radar.RadarMap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;

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
    public void worldUpdateTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.provider != null && event.side == Side.SERVER && event.phase == TickEvent.Phase.END)
        {
            int dim = event.world.provider.dimensionId;
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
        if (event.world.provider != null)
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
        if (event.tile() != null)
        {
            add(event.tile()); //TODO check if there was a tile already stored at location
        }
    }

    @SubscribeEvent
    public void onTileLoaded(TileEvent.TileUnLoadEvent event)
    {
        if (event.tile() != null)
        {
            remove(event.tile());
        }
    }
}
