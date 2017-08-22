package com.builtbroken.mc.lib.world.map.radar;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.lib.world.map.radar.data.RadarEntity;
import com.builtbroken.mc.lib.world.map.radar.data.RadarObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Map based system for tracking objects using a radar devices. Only works server side to prevent unwanted data from stacking up.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public final class RadarRegistry
{
    /** Used only for event calls */
    public static final RadarRegistry INSTANCE = new RadarRegistry();
    //TODO add client side version for mini-map like systems
    //TODO add per machine tracking map that uses line of sight so hills can block it's view. (Visible Area Cache in other words)
    /** World id to radar map */
    private static final HashMap<Integer, RadarMap> RADAR_MAPS = new HashMap();

    /**
     * Adds an entity to the radar map
     *
     * @param entity - entity
     * @return true if added
     */
    public static boolean add(Entity entity)
    {
        if (entity != null && !entity.isDead && entity.worldObj != null && !entity.worldObj.isRemote)
        {
            RadarMap map = getRadarMapForWorld(entity.worldObj);
            return map != null ? getRadarMapForWorld(entity.worldObj).add(entity) : false;
        }
        return false;
    }

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
     * @param entity - entity
     * @return true if removed
     */
    public static boolean remove(Entity entity)
    {
        if (entity != null && !entity.isDead && entity.worldObj != null)
        {
            RadarMap map = getRadarMapForWorld(entity.worldObj);
            return map != null ? getRadarMapForWorld(entity.worldObj).remove(entity) : false;
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
        if (!RADAR_MAPS.containsKey(dimID))
        {
            RadarMap map = new RadarMap(dimID);
            RADAR_MAPS.put(dimID, map);
            return map;
        }
        return RADAR_MAPS.get(dimID);
    }

    /**
     * Grabs all living radar objects within range
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param distance
     * @param selector - optional, used to refine list of entities
     * @return list, never null
     */
    public static List<Entity> getAllLivingObjectsWithin(World world, double x, double y, double z, double distance, IEntitySelector selector)
    {
        return getAllLivingObjectsWithin(world, new Cube(x - distance, Math.max(0, y - distance), z - distance, x + distance, Math.min(255, y + distance), z + distance), selector);
    }

    /**
     * Grabs all living radar objects within range
     *
     * @param world
     * @param cube  - area to search for contacts
     * @return list, never null
     */
    public static List<Entity> getAllLivingObjectsWithin(World world, Cube cube, IEntitySelector selector)
    {
        List<Entity> list = new ArrayList();
        if (RADAR_MAPS.containsKey(world.provider.dimensionId))
        {
            RadarMap map = getRadarMapForWorld(world);
            if (map != null)
            {
                List<RadarObject> objects = map.getRadarObjects(cube, true);
                for (RadarObject object : objects)
                {
                    if (object instanceof RadarEntity && object.isValid())
                    {
                        Entity entity = ((RadarEntity) object).entity;
                        if (entity != null && !entity.isDead && (selector == null || selector.isEntityApplicable(entity)))
                        {
                            list.add(entity);
                        }
                    }
                }
            }
            else if (world.isRemote && Engine.runningAsDev)
            {
                Engine.logger().error("RadarRegistry: Radar data can not be requested client side.", new RuntimeException());
            }
        }
        return list;
    }

    @SubscribeEvent
    public void chunkUnload(ChunkEvent.Unload event)
    {
        if (event.getChunk().worldObj != null && event.getChunk().worldObj.provider != null)
        {
            int dim = event.getChunk().worldObj.provider.dimensionId;
            if (RADAR_MAPS.containsKey(dim))
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
            if (RADAR_MAPS.containsKey(dim))
            {
                RadarMap map = getRadarMapForDim(dim);
                if (map.chunk_to_entities.isEmpty())
                {
                    RADAR_MAPS.remove(dim);
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
            if (RADAR_MAPS.containsKey(dim))
            {
                getRadarMapForDim(dim).unloadAll();
                RADAR_MAPS.remove(dim);
            }
        }
    }

}
