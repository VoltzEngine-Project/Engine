package universalelectricity.components.common.item;

import net.minecraft.item.Item;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.prefab.UETab;

public class ItemBasic extends Item
{
	public ItemBasic(String name, int id, int texture)
	{
		super(id);
		this.setIconIndex(texture);
		this.setItemName(name);
		this.setCreativeTab(UETab.INSTANCE);
		this.setTextureFile(BasicComponents.ITEM_TEXTURE_FILE);
	}
}