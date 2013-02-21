package basiccomponents.common.item;

import net.minecraft.item.ItemStack;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;
import basiccomponents.common.BasicComponents;

public class ItemBattery extends ItemElectric
{
	public ItemBattery(int id, int texture)
	{
		super(id);
		this.setIconIndex(texture);
		this.setItemName("battery");
		this.setCreativeTab(UETab.INSTANCE);
		this.setTextureFile(BasicComponents.ITEM_TEXTURE_FILE);
	}

	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		return 1000000;
	}

	@Override
	public double getVoltage(ItemStack itemStack)
	{
		return 25;
	}
}
