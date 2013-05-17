package calclavia.lib;

import ic2.api.item.ICustomElectricItem;
import net.minecraft.item.ItemStack;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.item.ItemElectric;

public abstract class ItemUniversalElectric extends ItemElectric implements ICustomElectricItem
{
	public static final float CHARGE_RATE = 0.005f;

	public ItemUniversalElectric(int id)
	{
		super(id);
	}

	/*
	 * public ItemUniversalElectric(int id, String name) { super(ZhuYao.CONFIGURATION.getItem(name,
	 * id).getInt()); this.setUnlocalizedName(ZhuYao.PREFIX + name);
	 * this.setCreativeTab(ICBMTab.INSTANCE); }
	 */

	@Override
	public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		double inputElectricity = amount * UniversalElectricity.IC2_RATIO;

		inputElectricity = Math.min(inputElectricity, this.getMaxJoules(itemStack) - this.getJoules(itemStack));

		if (!ignoreTransferLimit)
		{
			inputElectricity = Math.min(inputElectricity, this.getMaxJoules(itemStack) * CHARGE_RATE);
		}

		if (!simulate)
		{
			this.setJoules(this.getJoules(itemStack) + inputElectricity, itemStack);
		}

		return (int) (inputElectricity * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		double outputElectricity = amount * UniversalElectricity.IC2_RATIO;

		outputElectricity = Math.min(outputElectricity, this.getJoules(itemStack));

		if (!ignoreTransferLimit)
		{
			outputElectricity = Math.min(this.getJoules(itemStack), this.getMaxJoules(itemStack) * CHARGE_RATE);
		}

		if (!simulate)
		{
			this.setJoules(this.getJoules(itemStack) - outputElectricity, itemStack);
		}

		return (int) (outputElectricity * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public boolean canUse(ItemStack itemStack, int amount)
	{
		return false;
	}

	@Override
	public boolean canShowChargeToolTip(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return this.getProvideRequest(itemStack).getWatts() > 0;
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
		return (int) (this.getMaxJoules(itemStack) * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int getTier(ItemStack itemStack)
	{
		return 1;
	}

	@Override
	public int getTransferLimit(ItemStack itemStack)
	{
		return (int) ((this.getMaxJoules(itemStack) * CHARGE_RATE) * UniversalElectricity.TO_IC2_RATIO);
	}
}
