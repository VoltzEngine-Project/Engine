package universalelectricity.basiccomponents;

import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class OilBucketHandler
{
    @ForgeSubscribe
    public void onBucketFill(FillBucketEvent event)
    {
    	ItemStack result = fillCustomBucket(event.world,event.target);
    	
    	if(result == null) return;

    	event.result=result;
    	event.setHandeled();
    }
    
	public ItemStack fillCustomBucket(World worldObj, MovingObjectPosition position)
	{
		int blockID = worldObj.getBlockId(position.blockX,position.blockY,position.blockZ);

		if((blockID == BasicComponents.oilStill.blockID || blockID == BasicComponents.oilMoving.blockID) && worldObj.getBlockMetadata(position.blockX, position.blockY, position.blockZ) == 0)
		{
			worldObj.setBlockWithNotify(position.blockX, position.blockY, position.blockZ, 0);
			return new ItemStack(BasicComponents.itemOilBucket);
		}
		else
		{
			return null;
		}

	}

}