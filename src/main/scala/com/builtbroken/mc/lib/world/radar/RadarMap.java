package com.builtbroken.mc.lib.world.radar;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * System designed to track moving or stationary targets on a 2D map. Can be used to detect objects or visualize objects in an area. Mainly
 * used to track flying objects that are outside of the map bounds(Missile in ICBM).
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public class RadarMap
{
    /** DIM ID, never change */
    protected final int dimID;

    /** Map of chunk coords( converted to long) to radar contacts in that chunk */
    protected HashMap<ChunkCoordIntPair, List<RadarObject>> chunk_to_entities = new HashMap();


    /**
     * Dimension ID
     *
     * @param dimID - unique dimension that is not already tracked
     */
    public RadarMap(int dimID)
    {
        this.dimID = dimID;
    }

    /**
     * Called at the end of every world tick to do checks on
     * data stored.
     */
    public void update()
    {
    }

    public boolean add(Entity entity)
    {
        return add(new RadarEntity(entity));
    }

    public boolean add(TileEntity tile)
    {
        return add(new RadarTile(tile));
    }

    public boolean add(RadarObject object)
    {
        ChunkCoordIntPair pair = getChunkValue((int) object.x(), (int) object.y());
        List<RadarObject> list;

        //Get list or make new
        if (chunk_to_entities.containsKey(pair))
        {
            list = chunk_to_entities.get(pair);
        }
        else
        {
            list = new ArrayList();
        }

        //Check if object is not already added
        if (!list.contains(object))
        {
            list.add(object);
            //TODO fire map update event
            //TODO fire map add event
            //Update map
            chunk_to_entities.put(pair, list);
            return true;
        }
        return false;
    }

    public boolean remove(Entity entity)
    {
        return remove(new RadarEntity(entity));
    }

    public boolean remove(TileEntity tile)
    {
        return remove(new RadarTile(tile));
    }

    public boolean remove(RadarObject object)
    {
        ChunkCoordIntPair pair = getChunkValue((int) object.x(), (int) object.y());
        if (chunk_to_entities.containsKey(pair))
        {
            List<RadarObject> list = chunk_to_entities.get(pair);
            boolean b = list.remove(object);
            //TODO fire radar remove event
            //TODO fire map update event
            if (list.isEmpty())
            {
                chunk_to_entities.remove(pair);
            }
            return b;
        }
        return false;
    }

    /**
     * Removes all entries connected with the provided chunk location data
     *
     * @param chunk - should never be null
     */
    public void remove(Chunk chunk)
    {
        ChunkCoordIntPair pair = chunk.getChunkCoordIntPair();
        if (chunk_to_entities.containsKey(pair))
        {
            chunk_to_entities.remove(pair);
            //TODO go threw each object in list and fire remove event
        }
    }

    protected final ChunkCoordIntPair getChunkValue(int x, int z)
    {
        return new ChunkCoordIntPair(x >> 4, z >> 4);
    }

    public void unloadAll()
    {
        chunk_to_entities.clear();
    }

    /**
     * Dimension ID this map tracks
     *
     * @return valid dim ID.
     */
    public int dimID()
    {
        return dimID;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof RadarMap)
        {
            return ((RadarMap) object).dimID == dimID;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "RadarMap[" + dimID + "]";
    }
}
