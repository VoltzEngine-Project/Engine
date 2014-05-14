package calclavia.lib.asm;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import calclavia.lib.config.ConfigAnnotationEvent;
import calclavia.lib.event.ChunkModifiedEvent.ChunkSetBlockEvent;

/** @author Calclavia */
public class StaticForwarder
{
    public static void chunkSetBlockEvent(Chunk chunk, int x, int y, int z, int blockID, int blockMetadata)
    {
        MinecraftForge.EVENT_BUS.post(new ChunkSetBlockEvent(chunk, x, y, z, blockID, blockMetadata));
    }

    /** A Static forwarder to post an event of when a class is loaded, and contains a @Config
     * 
     * @param c Class to load data from */
    public static void onConfigClassLoad(Class c)
    {
        MinecraftForge.EVENT_BUS.post(new ConfigAnnotationEvent(c));
    }
}
