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

import java.io.File;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public abstract class ChunkMap<C extends ChunkData> implements IVirtualObject
{
    /** Dim ID, used to match the map to the world */
    public final int dimID;

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

    public void onWorldUnload()
    {
        //Use for cleanup
        //TODO save data
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

    public boolean shouldUnload()
    {
        return chunks.isEmpty();
    }

    @Override
    public File getSaveFile()
    {
        return mapManager.getSaveFile(dimID);
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

}
