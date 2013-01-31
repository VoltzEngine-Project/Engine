package basiccomponents.common.item;

import basiccomponents.common.BasicComponents;
import net.minecraft.item.Item;
import universalelectricity.prefab.UETab;

public class ItemBasic extends Item
{
	public ItemBasic(String name, int id, int texture)
	{
		super(id);
		this.setIconIndex(texture);
		this.setItemName(name);
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public String getTextureFile()
	{
		return BasicComponents.ITEM_TEXTURE_FILE;
	}
}