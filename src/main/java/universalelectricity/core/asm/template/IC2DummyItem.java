package universalelectricity.core.asm.template;

import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.item.IEnergyItem;

/**
 * @author Calclavia
 * 
 */
public class IC2DummyItem implements IElectricItemManager
{
	public static final IC2DummyItem INSTANCE = new IC2DummyItem();

	private IEnergyItem get(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof IEnergyItem)
		{
			return (IEnergyItem) itemStack.getItem();
		}

		return null;
	}

	@Override
	public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		if (get(itemStack) != null)
		{
			return (int) (get(itemStack).recharge(itemStack, (long) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio), !simulate) * CompatibilityType.INDUSTRIALCRAFT.ratio);
		}

		return 0;
	}

	@Override
	public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		if (get(itemStack) != null)
		{
			return (int) (get(itemStack).discharge(itemStack, (long) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio), !simulate) * CompatibilityType.INDUSTRIALCRAFT.ratio);
		}

		return 0;
	}

	@Override
	public int getCharge(ItemStack itemStack)
	{
		if (get(itemStack) != null)
		{
			return (int) (get(itemStack).getEnergy(itemStack) * CompatibilityType.INDUSTRIALCRAFT.ratio);
		}

		return 0;
	}

	@Override
	public boolean canUse(ItemStack itemStack, int amount)
	{
		if (get(itemStack) != null)
		{
			return get(itemStack).getEnergy(itemStack) > (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
		}

		return false;
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
