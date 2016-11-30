package com.builtbroken.mc.core.content.world.chunks;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
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
    /** World the generator functions inside. */
    protected final World worldObj;
    /** Name of the generator */
    protected final String name;

    /**
     * Creates a new generator instance
     *
     * @param world - world the generator works in
     * @param name  - name of the generator
     */
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

    /**
     * Generates a platform the same size as the chunk (16x16 1 block thick)
     *
     * @param chunk - chunk to edit
     * @param y     - y level to generate at
     * @param block - block to place
     * @param meta  - meta to place
     */
    protected final void generatePlatform(Chunk chunk, int y, Block block, int meta)
    {
        generatePlatform(chunk, y, 16, 16, 0, 0, block, meta);
    }

    /**
     * Generates a platform of the desired size
     *
     * @param chunk   - chunk to edit
     * @param y       - y level to generate at
     * @param sizeX   - size in the x axis (East- West)
     * @param -       size in the z axis (South-North)
     * @param startX  - start point to generate from
     * @param startZ- start point to generate from
     * @param block   - block to place
     * @param meta    - meta to place
     */
    protected final void generatePlatform(Chunk chunk, int y, int sizeX, int sizeZ, int startX, int startZ, Block block, int meta)
    {
        for (int cx = startX; cx < startX + sizeX; ++cx)
        {
            for (int cz = startZ; cz < startZ + sizeZ; ++cz)
            {
                setBlock(chunk, cx, y, cz, block, meta);
            }
        }
    }

    /**
     * Generate a rectangle shape that is not filled in, in other words the side of the rect only
     *
     * @param chunk   - chunk to edit
     * @param y       - y level to generate at
     * @param startX  - start point to generate from
     * @param startZ- start point to generate from
     * @param block   - block to place
     * @param meta    - meta to place
     */
    protected final void generateSquare(Chunk chunk, int y, int sizeX, int sizeZ, int startX, int startZ, Block block, int meta)
    {
        for (int cz = startZ; cz < startZ + sizeZ; ++cz)
        {
            setBlock(chunk, startX, y, cz, block, meta);
            setBlock(chunk, startX + sizeX - 1, y, cz, block, meta);
        }

        for (int cx = startX + 1; cx < startX + sizeX - 1; ++cx)
        {
            setBlock(chunk, cx, y, startZ, block, meta);
            setBlock(chunk, cx, y, startZ + sizeZ - 1, block, meta);
        }
    }

    /**
     * Generate corners of a square shape
     *
     * @param chunk - chunk to edit
     * @param y     - y level to generate at
     * @param size  - size of the square
     * @param block - block to place
     * @param meta  - meta to place
     */
    protected final void generateCorners(Chunk chunk, int y, int size, Block block, int meta)
    {
        generateCorners(chunk, y, size, 0, 0, block, meta);
    }

    /**
     * Generate corners of a square shape
     *
     * @param chunk   - chunk to edit
     * @param y       - y level to generate at
     * @param size    - size of the square
     * @param startX  - start point to generate from
     * @param startZ- start point to generate from
     * @param block   - block to place
     * @param meta    - meta to place
     */
    protected final void generateCorners(Chunk chunk, int y, int size, int startX, int startZ, Block block, int meta)
    {
        final int reduction = 15 - size;
        final int small = reduction / 2;
        final int large = 15 - small;
        generateCorners(chunk, y, small, large, startX, startZ, block, meta);
    }

    /**
     * Generate corners of a square shape
     *
     * @param chunk   - chunk to edit
     * @param y       - y level to generate at
     * @param small   - smallest corner point (Where the square generates)
     * @param large   - largest corner point
     * @param startX  - start point to generate from
     * @param startZ- start point to generate from
     * @param block   - block to place
     * @param meta    - meta to place
     */
    protected final void generateCorners(Chunk chunk, int y, int small, int large, int startX, int startZ, Block block, int meta)
    {
        setBlock(chunk, startX + small, y, startZ + small, block, meta);
        setBlock(chunk, startX + large, y, startZ + small, block, meta);
        setBlock(chunk, startX + small, y, startZ + large, block, meta);
        setBlock(chunk, startX + large, y, startZ + large, block, meta);
    }

    /**
     * Places a block inside of the chunks extends block storage
     * <p>
     * This will also set the light level if the block has a light level.
     *
     * @param chunk - chunk to edit
     * @param y     - y level to place the block at
     * @param x     - chunk internal x pos 0 to 15
     * @param z     - chunk internal z pos 0 to 15
     * @param block - block to place
     * @param meta  - meta to place
     */
    protected final void setBlock(Chunk chunk, int x, int y, int z, Block block, int meta)
    {
        final int l = y >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage((y / 16) * 16, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }

        extendedblockstorage.func_150818_a(x, y & 15, z, block);
        if (meta >= 0 && meta < 16)
        {
            extendedblockstorage.setExtBlockMetadata(x, y & 15, z, meta);
        }
        if (block.getLightValue() > 0)
        {
            extendedblockstorage.setExtBlocklightValue(x, y & 15, z, block.getLightValue());
        }
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
