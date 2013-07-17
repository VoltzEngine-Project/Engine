package universalelectricity.compatibility;

import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;

public class IC2ElectricItemManager implements IElectricItemManager
{
	public static final IElectricItemManager MANAGER = new IC2ElectricItemManager();

	private IItemElectric getElectricItem(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof IItemElectric)
		{
			return ((IItemElectric) itemStack.getItem());
		}
		return null;
	}

	@Override
	public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		float inputElectricity = amount * Compatibility.IC2_RATIO;
		return (int) (getElectricItem(itemStack).recharge(itemStack, inputElectricity, !simulate) * Compatibility.TO_IC2_RATIO);
	}

	@Override
	public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		float outputElectricity = amount * Compatibility.IC2_RATIO;
		return (int) (getElectricItem(itemStack).discharge(itemStack, outputElectricity, !simulate) * Compatibility.TO_IC2_RATIO);
	}

	@Override
	public boolean canUse(ItemStack itemStack, int amount)
	{
		return false;
	}

	@Override
	public int getCharge(ItemStack itemStack)
	{
		return 0;
	}

	@Override
	public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
	{

	}

	@Override
	public String getToolTip(ItemStack itemStack)
	{
		return null;
	}

}
