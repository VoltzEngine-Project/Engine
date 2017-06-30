package com.builtbroken.mc.lib.world.map.data;

import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.SaveManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public abstract class ChunkMap<C extends ChunkData> implements IVirtualObject
{
    /** Dim ID, used to match the map to the world */
    public int dimID;

    protected long ticks = 0L;

    /** Loaded chunks */
    public final HashMap<ChunkCoordIntPair, C> chunks = new HashMap();

    public final ChunkMapManager mapManager;

    public ChunkMap(ChunkMapManager mapManager, int dimID)
    {
        this.mapManager = mapManager;
        this.dimID = dimID;
        if (!Engine.isJUnitTest())
        {
            MinecraftForge.EVENT_BUS.register(this);
            SaveManager.register(this);
        }
    }

    /**
     * Called each world tick to update
     */
    public void update()
    {
        if (ticks % 200 == 0)
        {
            //TODO check if chunks are still loaded
        }
    }

    public C getChunkFromBlockCoords(int x, int z)
    {
        return getChunk(x >> 4, z >> 4);
    }

    /**
     * Gets the chunk if it exists
     *
     * @param x
     * @param z
     * @return chunk, or null if missing
     */
    public C getChunk(int x, int z)
    {
        ChunkCoordIntPair coords = new ChunkCoordIntPair(x, z);
        if (chunks.containsKey(coords))
        {
            return chunks.get(coords);
        }
        return null;
    }

    protected C add(C chunk)
    {
        chunks.put(chunk.position, chunk);
        return chunk;
    }

    @SubscribeEvent
    public void onChunkLoaded(ChunkEvent.Load event)
    {
        Chunk chunk = event.getChunk();
        ChunkCoordIntPair coords = chunk.getChunkCoordIntPair();
        if (chunks.containsKey(coords))
        {
            //TODO load data into existing object
        }
        else
        {
            //TODO load object
        }
    }

    @SubscribeEvent
    public void onChunkUnloaded(ChunkEvent.Unload event)
    {
        Chunk chunk = event.getChunk();
        ChunkCoordIntPair coords = chunk.getChunkCoordIntPair();
        if (chunks.containsKey(coords))
        {
            //TODO save data
            chunks.remove(coords);
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        if (event.world.provider.dimensionId == dimID)
        {
            saveAll();
        }
    }

    /**
     * Called when the world has be unloaded from
     * the game. At this time everything should be
     * saved and caches cleared.
     */
    public void onWorldUnload()
    {
        saveAll();
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

    /**
     * Called to check if this map should be unloaded to
     * save memory.
     * <p>
     * Conditions that validate for unload are if the map
     * is empty or has not been used for an extended period
     * of time. In which unloading it would free up memory
     * for other data systems to use.
     *
     * @return true if should unload
     */
    public boolean shouldUnload()
    {
        return chunks.isEmpty();
    }

    @Override
    public File getSaveFile()
    {
        return mapManager != null ? mapManager.getSaveFile(dimID) : null;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    /**
     * Called after the map has been loaded from disk
     * should be used to load chunk data.
     */
    public void onLoad()
    {
        //TODO load chunk data
    }


    public void saveAll()
    {
        //TODO load chunk data
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }

    @Override
    public boolean shouldSaveForWorld(World world)
    {
        return dimID == world.provider.dimensionId;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object.getClass() == getClass())
        {
            return ((ChunkMap) object).dimID == dimID;
        }
        return false;
    }

    public void clear()
    {
        chunks.clear();
    }
}
