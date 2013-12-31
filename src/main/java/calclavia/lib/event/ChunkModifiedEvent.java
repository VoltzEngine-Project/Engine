package calclavia.lib.event;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

/**
 * Called when the chunk is modified with set block.
 * 
 * @author Calclavia
 * 
 */
public abstract class ChunkModifiedEvent extends ChunkEvent
{
	public ChunkModifiedEvent(Chunk chunk)
	{
		super(chunk);
	}

	public static class ChunkSetBlockEvent extends ChunkModifiedEvent
	{
		public final int x, y, z, blockID, blockMetadata;

		public ChunkSetBlockEvent(Chunk chunk, int x, int y, int z, int blockID, int blockMetadata)
		{
			super(chunk);
			this.x = x;
			this.y = y;
			this.z = z;
			this.blockID = blockID;
			this.blockMetadata = blockMetadata;
		}

	}
}
