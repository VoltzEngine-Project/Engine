package universalelectricity.basiccomponents;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucket;

public class ItemOilBucket extends ItemBucket
{
	public ItemOilBucket(String name, int id, int texture)
	{
		super(id, BasicComponents.oilMoving.blockID);
		this.setIconIndex(texture);
		this.setTabToDisplayOn(CreativeTabs.tabMisc);
		this.setContainerItem(Item.bucketEmpty);
	}
	
    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }
}