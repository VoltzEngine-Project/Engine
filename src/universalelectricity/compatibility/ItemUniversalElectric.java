package universalelectricity.compatibility;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.item.ItemStack;
import thermalexpansion.api.item.IChargeableItem;
import universalelectricity.core.item.ItemElectric;

public abstract class ItemUniversalElectric extends ItemElectric implements ISpecialElectricItem, IChargeableItem
{
	public static final float CHARGE_RATE = 0.005f;

	public ItemUniversalElectric(int id)
	{
		super(id);
	}

	/**
	 * IC2
	 */
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
		return (int) (this.getMaxElectricityStored(itemStack) * Compatibility.TO_IC2_RATIO);
	}

	@Override
	public int getTier(ItemStack itemStack)
	{
		return 1;
	}

	@Override
	public int getTransferLimit(ItemStack itemStack)
	{
		return (int) ((this.getMaxElectricityStored(itemStack) * CHARGE_RATE) * Compatibility.TO_IC2_RATIO);
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

	/**
	 * Thermal Expansion
	 */
	@Override
	public float receiveEnergy(ItemStack theItem, float energy, boolean doReceive)
	{
		return this.recharge(theItem, energy * Compatibility.BC3_RATIO, doReceive) * Compatibility.TO_BC_RATIO;
	}

	@Override
	public float transferEnergy(ItemStack theItem, float energy, boolean doTransfer)
	{
		return this.discharge(theItem, energy * Compatibility.BC3_RATIO, doTransfer) * Compatibility.TO_BC_RATIO;
	}

	@Override
	public float getEnergyStored(ItemStack theItem)
	{
		return this.getElectricityStored(theItem) * Compatibility.TO_BC_RATIO;
	}

	@Override
	public float getMaxEnergyStored(ItemStack theItem)
	{
		return this.getMaxElectricityStored(theItem) * Compatibility.TO_BC_RATIO;
	}

}
