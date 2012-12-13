package basiccomponents.common.item;

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
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return 20000;
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
	public double getVoltage()
	{
		return 25;
	}
}
