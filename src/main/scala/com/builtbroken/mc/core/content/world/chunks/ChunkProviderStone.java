package com.builtbroken.mc.core.content.world.chunks;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.Arrays;

public class ChunkProviderStone extends AbstractChunkProvider
{
    public ChunkProviderStone(World world)
    {
        super(world, "stoneTestWorldGenerator");
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(int chunkX, int chunkZ)
    {
        Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);

        int y = 0;
        //Bedrock to prevent falling out of the world
        generatePlatform(chunk, y++, Blocks.bedrock);
        generatePlatform(chunk, y++, Blocks.bedrock);
        generatePlatform(chunk, y++, Blocks.bedrock);
        generatePlatform(chunk, y++, Blocks.bedrock);
        generatePlatform(chunk, y++, Blocks.bedrock);

        //20 layers of stone
        for (int i = 0; i <= 20; i++)
        {
            generatePlatform(chunk, y++, Blocks.cobblestone);
            generatePlatform(chunk, y++, Blocks.stone);
        }

        //Top layer and chunk boards
        generatePlatform(chunk, y, Blocks.stone);
        generateSquare(chunk, y, 16, 16, 0, 0, Blocks.coal_block);

        final int l = y >> 4;
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];
        generateCorners(chunk, y, 9, Blocks.glowstone);
        generateCorners(chunk, y, 5, 10, 0, 0, Blocks.glowstone);

        extendedblockstorage.func_150818_a(0, y & 15, 2, Blocks.glowstone);
        extendedblockstorage.func_150818_a(0, y & 15, 13, Blocks.glowstone);

        extendedblockstorage.func_150818_a(15, y & 15, 2, Blocks.glowstone);
        extendedblockstorage.func_150818_a(15, y & 15, 13, Blocks.glowstone);

        extendedblockstorage.func_150818_a(2, y & 15, 15, Blocks.glowstone);
        extendedblockstorage.func_150818_a(13, y & 15, 15, Blocks.glowstone);

        extendedblockstorage.func_150818_a(2, y & 15, 0, Blocks.glowstone);
        extendedblockstorage.func_150818_a(13, y & 15, 0, Blocks.glowstone);

        //Not sure if we need to call this before biome
        chunk.generateSkylightMap();

        //Set biome ID for chunk
        byte[] abyte1 = chunk.getBiomeArray();
        Arrays.fill(abyte1, (byte) 1);

        chunk.generateSkylightMap();
        return chunk;
    }
}