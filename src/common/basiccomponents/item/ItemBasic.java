package basiccomponents.item;

import net.minecraft.src.Item;
import universalelectricity.prefab.UETab;
import basiccomponents.BasicComponents;
import cpw.mods.fml.common.registry.LanguageRegistry;

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