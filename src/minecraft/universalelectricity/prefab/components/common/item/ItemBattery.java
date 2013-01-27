package universalelectricity.prefab.components.common.item;

import net.minecraft.item.ItemStack;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.components.common.BasicComponents;

public class ItemBattery extends ItemElectric
{
	public ItemBattery(int par1, int par2)
	{
		super(par1);
		this.iconIndex = par2;
		this.setItemName("battery");
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		return 1000000;
	}

	@Override
	public boolean canProduceElectricity()
	{
		return true;
	}

	@Override
	public String getTextureFile()
	{
		return BasicComponents.ITEM_TEXTURE_FILE;
	}

	@Override
	public double getVoltage(ItemStack itemStack)
	{
		return 25;
	}
}
