package com.builtbroken.mc.lib.world.map.data;

import com.builtbroken.mc.core.Engine;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public abstract class ChunkMap<C extends ChunkData>
{
    /** Dim ID, used to match the map to the world */
    public final int dimID;

    /** Loaded chunks */
    public final HashMap<ChunkCoordIntPair, C> chunks = new HashMap();

    public ChunkMap(int dimID)
    {
        this.dimID = dimID;
        if (!Engine.isJUnitTest())
        {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public C getChunkFromBlockCoords(int x, int z)
    {
        return getChunk(x << 4, z << 4);
    }

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
        return null;
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
        else if (object.getClass() == getClass())
        {
            return ((ChunkMap) object).dimID == dimID;
        }
        return false;
    }
}
