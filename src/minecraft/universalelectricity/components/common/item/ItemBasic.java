package universalelectricity.components.common.item;

import net.minecraft.item.Item;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.prefab.UETab;

public class ItemBasic extends Item
{
	public ItemBasic(String name, int id)
	{
		super(id);
		this.setUnlocalizedName(name);
		this.setCreativeTab(UETab.INSTANCE);
	}
}