package calclavia.lib;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.item.ItemStack;
import universalelectricity.compatiblity.Compatiblity;
import universalelectricity.core.item.ItemElectric;

public abstract class ItemUniversalElectric extends ItemElectric implements ISpecialElectricItem
{
	public static final float CHARGE_RATE = 0.005f;

	public ItemUniversalElectric(int id)
	{
		super(id);
	}

	@Override
	public int getChargedItemId(ItemStack itemStack)
	{
		return this.itemID;
	}

	@Override
	public int getEmptyItemId(ItemStack itemStack)
	{
		return this.itemID;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return (int) (this.getMaxElectricityStored(itemStack) * Compatiblity.TO_IC2_RATIO);
	}

	@Override
	public int getTier(ItemStack itemStack)
	{
		return 1;
	}

	@Override
	public int getTransferLimit(ItemStack itemStack)
	{
		return (int) ((this.getMaxElectricityStored(itemStack) * CHARGE_RATE) * Compatiblity.TO_IC2_RATIO);
	}

	@Override
	public IElectricItemManager getManager(ItemStack itemStack)
	{
		return IC2ElectricItemManager.MANAGER;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return this.recharge(itemStack, 1, false) > 0;
	}

}
