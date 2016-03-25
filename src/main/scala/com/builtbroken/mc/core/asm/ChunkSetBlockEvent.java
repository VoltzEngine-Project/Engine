package com.builtbroken.mc.core.asm;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

/**
 * Simple Event fired when a chunk is modified with a set block event
 * Created by robert on 2/19/2015.
 */
public class ChunkSetBlockEvent extends ChunkEvent
{
    public final int x, y, z;
    public final Block block;
    public final int meta;


    public ChunkSetBlockEvent(Chunk chunk, int x, int y, int z, Block block, int meta)
    {
        super(chunk);
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.meta = meta;
    }
}
