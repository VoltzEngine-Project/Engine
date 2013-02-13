package basiccomponents.common.item;

import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;
import basiccomponents.common.BasicComponents;

public class ItemBattery extends ItemElectric
{
	public ItemBattery(int par1, int par2)
	{
		super(par1);
		this.iconIndex = par2;
		this.setItemName("battery");
		this.setCreativeTab(UETab.INSTANCE);
		this.setTextureFile(BasicComponents.ITEM_TEXTURE_FILE);
	}

	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		return 1000000;
	}
}
