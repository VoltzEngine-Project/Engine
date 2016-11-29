package com.builtbroken.mc.core.content.world.chunks;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.Arrays;

public class ChunkProviderEmpty extends AbstractChunkProvider
{
    public ChunkProviderEmpty(World world)
    {
        super(world, "voidTestWorld");
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(int chunkX, int chunkZ)
    {
        Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
        int spawnChunkX = this.worldObj.getSpawnPoint().posX >> 4;
        int spawnChunkZ = this.worldObj.getSpawnPoint().posZ >> 4;
        int y_base = 70;

        //Init spawn if it has not already been created
        if (spawnChunkX == chunkX && spawnChunkZ == chunkZ)
        {
            generateSpawnCenter(chunk, chunkX, chunkZ, y_base);
        }
        else if (spawnChunkX + 1 == chunkX && spawnChunkZ == chunkZ || spawnChunkX - 1 == chunkX && spawnChunkZ == chunkZ)
        {
            generateXBridge(chunk, chunkX, chunkZ, y_base);
        }
        else if (spawnChunkX == chunkX && spawnChunkZ + 1 == chunkZ || spawnChunkX == chunkX && spawnChunkZ - 1 == chunkZ)
        {
            generateZBridge(chunk, chunkX, chunkZ, y_base);
        }
        else if (spawnChunkX + 2 == chunkX && spawnChunkZ == chunkZ)
        {
            generatePlatform(chunk, chunkX, chunkZ, y_base);
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y_base >> 4];
            extendedblockstorage.func_150818_a(15, (y_base + 11) & 15, 8, Blocks.command_block);
        }
        else if (spawnChunkX - 2 == chunkX && spawnChunkZ == chunkZ)
        {
            generatePlatform(chunk, chunkX, chunkZ, y_base);
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y_base >> 4];
            extendedblockstorage.func_150818_a(0, (y_base + 11) & 15, 8, Blocks.command_block);
        }
        else if (spawnChunkX == chunkX && spawnChunkZ + 2 == chunkZ)
        {
            generatePlatform(chunk, chunkX, chunkZ, y_base);
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y_base >> 4];
            extendedblockstorage.func_150818_a(8, (y_base + 11) & 15, 15, Blocks.command_block);
        }
        else if (spawnChunkX == chunkX && spawnChunkZ - 2 == chunkZ)
        {
            generatePlatform(chunk, chunkX, chunkZ, y_base);
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y_base >> 4];
            extendedblockstorage.func_150818_a(8, (y_base + 11) & 15, 0, Blocks.command_block);
        }
        else if (spawnChunkX + 4 == chunkX && spawnChunkZ == chunkZ)
        {
            lazyGenerateIsland("chunkFlat", chunkX << 4, y_base, chunkZ << 4);
        }
        else if (spawnChunkX - 4 == chunkX && spawnChunkZ == chunkZ)
        {
            lazyGenerateIsland("tree", chunkX << 4, y_base, chunkZ << 4);
        }
        else if (spawnChunkX == chunkX && spawnChunkZ + 4 == chunkZ)
        {
            lazyGenerateIsland("stonebrickPlatform", chunkX << 4, y_base, chunkZ << 4);
        }
        else if (spawnChunkX == chunkX && spawnChunkZ - 4 == chunkZ)
        {
            //lazyGenerateIsland("chunkFlat", chunkX << 4, 33, chunkZ << 4);
        }

        //Not sure if we need to call this before biome
        chunk.generateSkylightMap();

        //Set biome ID for chunk
        byte[] abyte1 = chunk.getBiomeArray();
        Arrays.fill(abyte1, (byte)1);

        chunk.generateSkylightMap();
        return chunk;
    }

    private void lazyGenerateIsland(String type, int x, int y, int z)
    {

    }

    private void generatePlatform(Chunk chunk, int chunkX, int chunkZ, int y_base)
    {
        int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }

        int k = y_base + 10;
        for (int cx = 0; cx < 16; ++cx)
        {
            for (int cz = 0; cz < 16; ++cz)
            {
                extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stone);
            }
        }

        k = y_base + 9;
        for (int cx = 1; cx < 15; ++cx)
        {
            for (int cz = 1; cz < 15; ++cz)
            {
                extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stone);
            }
        }

        k = y_base + 8;
        for (int cx = 2; cx < 14; ++cx)
        {
            for (int cz = 1; cz < 15; ++cz)
            {
                extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stone);
            }
        }
    }

    private void generateXBridge(Chunk chunk, int chunkX, int chunkZ, int y_base)
    {
        int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }

        int k = y_base + 10;
        for (int cx = 0; cx < 16; ++cx)
        {
            for (int cz = 6; cz < 10; ++cz)
            {
                if (cz == 6 || cz == 9)
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stonebrick);
                else
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.planks);

            }
        }
    }

    private void generateZBridge(Chunk chunk, int chunkX, int chunkZ, int y_base)
    {
        int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }

        int k = y_base + 10;
        for (int cx = 6; cx < 10; ++cx)
        {
            for (int cz = 0; cz < 16; ++cz)
            {
                if (cx == 6 || cx == 9)
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stonebrick);
                else
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.planks);

            }
        }
    }

    private void generateSpawnCenter(Chunk chunk, int chunkX, int chunkZ, int y_base)
    {
        //Generate spawn platform
        int l = y_base >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = new ExtendedBlockStorage(y_base, !this.worldObj.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
        }

        int k = y_base + 10;
        for (int cx = 0; cx < 16; ++cx)
        {
            for (int cz = 0; cz < 16; ++cz)
            {
                //corners of glowstone
                if ((cx == 0 || cx == 15) && (cz == 0 || cz == 15))
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.glowstone);
                //edges of stone bricks excluding bridge connections
                else if ((cx == 0 || cx == 15 || cz == 0 || cz == 15) && cz != 7 && cz != 8 && cx != 7 && cx != 8)
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stonebrick);
                //center of grass
                else if ((cx == 8 || cx == 7) && (cz == 8 || cz == 7))
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.grass);
                //reset stone
                else
                    extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stone);
            }
        }

        k = y_base + 9;
        for (int cx = 0; cx < 16; ++cx)
        {
            for (int cz = 0; cz < 16; ++cz)
            {
                extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stone);
                //extendedblockstorage.setExtBlockMetadata(cx, k & 15, cz, 0);
            }
        }

        k = y_base + 8;
        for (int cx = 1; cx < 15; ++cx)
        {
            for (int cz = 1; cz < 15; ++cz)
            {
                extendedblockstorage.func_150818_a(cx, k & 15, cz, Blocks.stone);
                //extendedblockstorage.setExtBlockMetadata(cx, k & 15, cz, 0);
            }
        }

        //Fix spawn point
        int x = (chunkX << 4) + 8;
        int z = (chunkZ << 4) + 8;
        int y = y_base + 12;

        this.worldObj.setSpawnLocation(x, y, z);
    }
}