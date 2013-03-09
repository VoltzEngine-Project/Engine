package universalelectricity.components.common.item;

import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;
import universalelectricity.prefab.UETab;

public class ItemBattery extends ItemElectric
{
	public ItemBattery(int id)
	{
		super(id);
		this.setUnlocalizedName("battery");
		this.setCreativeTab(UETab.INSTANCE);
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
