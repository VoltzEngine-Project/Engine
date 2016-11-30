package com.builtbroken.mc.core.content.world.chunks;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Arrays;

public class ChunkProviderChess extends AbstractChunkProvider
{
    public ChunkProviderChess(World world)
    {
        super(world, "stoneTestWorldGenerator");
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(final int chunkX, final int chunkZ)
    {
        final Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);

        int y = 0;
        //Bedrock to prevent falling out of the world
        generatePlatform(chunk, y++, Blocks.bedrock, -1);
        generatePlatform(chunk, y++, Blocks.bedrock, -1);
        generatePlatform(chunk, y++, Blocks.bedrock, -1);
        generatePlatform(chunk, y++, Blocks.bedrock, -1);
        generatePlatform(chunk, y++, Blocks.bedrock, -1);


        //20 layers of stone
        for (int i = 0; i <= 20; i++)
        {
            generatePlatform(chunk, y++, Blocks.cobblestone, -1);
            generatePlatform(chunk, y++, Blocks.stone, -1);
        }

        if (Math.abs(chunkX) % 2 == 0)
        {
            if (Math.abs(chunkZ) % 2 == 0)
            {
                generatePlatform(chunk, y++, Blocks.wool, 8);
            }
            else
            {
                generatePlatform(chunk, y++, Blocks.wool, 15);
            }
        }
        else
        {
            if (Math.abs(chunkZ) % 2 == 1)
            {
                generatePlatform(chunk, y++, Blocks.wool, 8);
            }
            else
            {
                generatePlatform(chunk, y++, Blocks.wool, 15);
            }
        }
        //Not sure if we need to call this before biome
        chunk.generateSkylightMap();

        //Set biome ID for chunk
        byte[] abyte1 = chunk.getBiomeArray();
        Arrays.fill(abyte1, (byte) 1);

        chunk.generateSkylightMap();
        return chunk;
    }
}