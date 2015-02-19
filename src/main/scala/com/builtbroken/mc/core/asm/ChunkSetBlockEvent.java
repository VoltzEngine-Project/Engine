package com.builtbroken.mc.core.asm;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

/**
 * Created by robert on 2/19/2015.
 */
public class ChunkSetBlockEvent extends ChunkEvent
{
    public ChunkSetBlockEvent(Chunk chunk, int x, int y, int z, Block block, int blockMetadata)
    {
        super(chunk);
    }
}
