package resonant.lib.event;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

/** Called when the chunk is modified with set block.
 * 
 * @author Calclavia */
public abstract class ChunkModifiedEvent extends ChunkEvent
{
    public ChunkModifiedEvent(Chunk chunk)
    {
        super(chunk);
    }

    public static class ChunkSetBlockEvent extends ChunkModifiedEvent
    {
        public final int x, y, z, blockID, blockMetadata;

        public ChunkSetBlockEvent(Chunk chunk, int chunkX, int y, int chunkZ, int blockID, int blockMetadata)
        {
            super(chunk);
            this.x = (chunk.xPosition << 4) + chunkX;
            this.y = y;
            this.z = (chunk.zPosition << 4) + chunkZ;
            this.blockID = blockID;
            this.blockMetadata = blockMetadata;
        }

    }
}
