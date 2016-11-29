package com.builtbroken.mc.core.content.world.chunks;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.List;

/**
 * Prefab to be used for chunk providers that do really basic generation
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/29/2016.
 */
public abstract class AbstractChunkProvider implements IChunkProvider
{
    protected final World worldObj;
    protected final String name;

    public AbstractChunkProvider(World world, String name)
    {
        this.worldObj = world;
        this.name = name;
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    @Override
    public Chunk loadChunk(int x, int z)
    {
        return this.provideChunk(x, z);
    }


    protected final void generatePlatform(Chunk chunk, int y_base, Block block)
    {
        generatePlatform(chunk, y_base, 16, 16, 0, 0, block);
    }

    protected final void generatePlatform(Chunk chunk, int y_base, int sizeX, int sizeZ, int startX, int startZ, Block block)
    {
        final int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }


        for (int cx = startX; cx < startX + sizeX; ++cx)
        {
            for (int cz = startZ; cz < startZ + sizeZ; ++cz)
            {
                extendedblockstorage.func_150818_a(cx, y_base & 15, cz, block);
            }
        }
    }

    protected final void generateSquare(Chunk chunk, int y_base, int sizeX, int sizeZ, int startX, int startZ, Block block)
    {
        final int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }

        for (int cz = startZ; cz < startZ + sizeZ; ++cz)
        {
            extendedblockstorage.func_150818_a(startX, y_base & 15, cz, block);
            extendedblockstorage.func_150818_a(startX + sizeX - 1, y_base & 15, cz, block);
        }

        for (int cx = startX + 1; cx < startX + sizeX - 1; ++cx)
        {
            extendedblockstorage.func_150818_a(cx, y_base & 15, startZ, block);
            extendedblockstorage.func_150818_a(cx, y_base & 15, startZ + sizeZ - 1, block);
        }
    }

    protected final void generateCorners(Chunk chunk, int y_base, int size, Block block)
    {
        generateCorners(chunk, y_base, size, 0, 0, block);
    }

    protected final void generateCorners(Chunk chunk, int y_base, int size, int startX, int startZ, Block block)
    {
        final int reduction = 15 - size;
        final int small = reduction / 2;
        final int large = 15 - small;
        generateCorners(chunk, y_base, small, large, startX, startZ, block);
    }

    protected final void generateCorners(Chunk chunk, int y_base, int small, int large, int startX, int startZ, Block block)
    {
        final int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }
        extendedblockstorage.func_150818_a(startX + small, y_base & 15, startZ + small, Blocks.glowstone);
        extendedblockstorage.func_150818_a(startX + large, y_base & 15, startZ + small, Blocks.glowstone);
        extendedblockstorage.func_150818_a(startX + small, y_base & 15, startZ + large, Blocks.glowstone);
        extendedblockstorage.func_150818_a(startX + large, y_base & 15, startZ + large, Blocks.glowstone);
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {

    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    @Override
    public void saveExtraData()
    {
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    @Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    @Override
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    @Override
    public String makeString()
    {
        return name;
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    @Override
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_)
    {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }

    @Override
    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_)
    {
        return null;
    }

    @Override
    public int getLoadedChunkCount()
    {
        return 0;
    }

    @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_)
    {

    }
}
