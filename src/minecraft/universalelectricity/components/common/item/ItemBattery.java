package universalelectricity.components.common.item;

import net.minecraft.item.ItemStack;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;

public class ItemBattery extends ItemElectric
{
	public ItemBattery(int id, int texture)
	{
		super(id);
		this.setIconIndex(texture);
		this.setUnlocalizedName("battery");
		this.setCreativeTab(UETab.INSTANCE);
		this.setTextureFile(BasicComponents.ITEM_TEXTURE_DIRECTORY);
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
