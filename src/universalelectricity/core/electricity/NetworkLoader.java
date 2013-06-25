package universalelectricity.core.electricity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import universalelectricity.core.block.IConductor;

public class NetworkLoader
{
	static
	{
		MinecraftForge.EVENT_BUS.register(new NetworkLoader());
	}

	@ForgeSubscribe
	public void onChunkLoad(ChunkEvent.Load event)
	{
		if (event.getChunk() != null)
		{
			for (Object obj : event.getChunk().chunkTileEntityMap.values())
			{
				if (obj instanceof TileEntity)
				{
					TileEntity tileEntity = (TileEntity) obj;

					if (tileEntity instanceof IConductor)
					{
						((IConductor) tileEntity).updateAdjacentConnections();
					}
				}
			}
		}
	}
}