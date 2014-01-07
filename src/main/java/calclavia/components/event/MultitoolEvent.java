package calclavia.components.event;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event.HasResult;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author Calclavia
 * 
 */
@Cancelable
@HasResult
public class MultitoolEvent extends BlockEvent
{
	private int side;
	private float hitX, hitY, hitZ;

	public MultitoolEvent(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block block, int blockMetadata)
	{
		super(x, y, z, world, block, blockMetadata);
		this.side = side;
		this.hitX = hitX;
		this.hitY = hitY;
		this.hitZ = hitZ;
	}

}
