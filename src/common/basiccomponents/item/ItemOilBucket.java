package basiccomponents.item;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucket;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import basiccomponents.BasicComponents;

public class ItemOilBucket extends ItemBucket
{
	public ItemOilBucket(String name, int id, int texture)
	{
		super(id, BasicComponents.oilMoving.blockID);
		this.setIconIndex(texture);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setContainerItem(Item.bucketEmpty);
		this.setItemName("Oil Bucket");
	}
	
    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }
    
    @ForgeSubscribe
    public void onBucketFill(FillBucketEvent event)
    {
    	if(event.current.itemID == Item.bucketEmpty.shiftedIndex)
    	{
	    	World worldObj = event.world;
	    	MovingObjectPosition position = event.target;
	    	
	    	int blockID = worldObj.getBlockId(position.blockX, position.blockY, position.blockZ);
	
			if((blockID == BasicComponents.oilStill.blockID || blockID == BasicComponents.oilMoving.blockID) && worldObj.getBlockMetadata(position.blockX, position.blockY, position.blockZ) == 0)
			{
				worldObj.setBlockWithNotify(position.blockX, position.blockY, position.blockZ, 0);
				event.result = new ItemStack(BasicComponents.itemOilBucket);
				event.current.stackSize --;
				event.setResult(Result.ALLOW);
			}
    	}
    }
}