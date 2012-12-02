package universalelectricity.prefab.manual;

import universalelectricity.prefab.UETab;
import net.minecraft.src.Item;

public class ItemEngineerBooklet extends Item
{
	public ItemEngineerBooklet(int id)
	{
		super(id);
		this.setCreativeTab(UETab.INSTANCE);
		this.setMaxStackSize(1);
	}

}
