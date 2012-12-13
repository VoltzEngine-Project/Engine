package basiccomponents.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import universalelectricity.prefab.UETab;
import basiccomponents.common.BasicComponents;

public class ItemOilBucket extends ItemBucket
{
	public ItemOilBucket(int id, int texture)
	{
		super(id, BasicComponents.oilMoving.blockID);
		this.setIconIndex(texture);
		this.setCreativeTab(UETab.INSTANCE);
		this.setContainerItem(Item.bucketEmpty);
		this.setItemName("bucketOil");
	}

	@Override
	public String getTextureFile()
	{
		return BasicComponents.ITEM_TEXTURE_FILE;
	}

	@ForgeSubscribe
	public void onBucketFill(FillBucketEvent event)
	{
		if (event.current.itemID == Item.bucketEmpty.shiftedIndex)
		{
			World worldObj = event.world;
			MovingObjectPosition position = event.target;

			int blockID = worldObj.getBlockId(position.blockX, position.blockY, position.blockZ);

			if ((blockID == BasicComponents.oilStill.blockID || blockID == BasicComponents.oilMoving.blockID) && worldObj.getBlockMetadata(position.blockX, position.blockY, position.blockZ) == 0)
			{
				worldObj.setBlockWithNotify(position.blockX, position.blockY, position.blockZ, 0);
				event.result = new ItemStack(BasicComponents.itemOilBucket);
				event.current.stackSize--;
				event.setResult(Result.ALLOW);
			}
		}
	}
}