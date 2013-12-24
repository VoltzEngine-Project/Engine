package universalelectricity.compatibility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.item.IEnergyItem;

/** @author Calclavia */
public class ModuleUniversalElectricity extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object handler, ForgeDirection direction, long energy, boolean doReceive)
	{
		return ((IEnergyInterface) handler).onReceiveEnergy(direction, energy, doReceive);
	}

	@Override
	public long doExtractEnergy(Object handler, ForgeDirection direction, long energy, boolean doExtract)
	{
		return ((IEnergyInterface) handler).onExtractEnergy(direction, energy, doExtract);
	}

	@Override
	public boolean doIsHandler(Object obj)
	{
		return obj instanceof IEnergyInterface || obj instanceof IEnergyItem;
	}

	@Override
	public boolean doCanConnect(Object obj, ForgeDirection direction)
	{
		return ((IEnergyInterface) obj).canConnect(direction);
	}

	@Override
	public long doChargeItem(ItemStack itemStack, long joules, boolean doCharge)
	{
		if (itemStack != null && itemStack.getItem() instanceof IEnergyItem)
		{
			return ((IEnergyItem) itemStack.getItem()).recharge(itemStack, joules, doCharge);
		}
		return 0;
	}

	@Override
	public long doDischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
	{
		if (itemStack != null && itemStack.getItem() instanceof IEnergyItem)
		{
			return ((IEnergyItem) itemStack.getItem()).discharge(itemStack, joules, doDischarge);
		}
		return 0;
	}

	@Override
	public ItemStack doGetItemWithCharge(ItemStack itemStack, long energy)
	{
		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof IEnergyItem)
			{
				((IEnergyItem) itemStack.getItem()).setEnergy(itemStack, energy);
				return itemStack;
			}
		}

		return itemStack;
	}

	@Override
	public boolean doIsEnergyContainer(Object obj)
	{
		return obj instanceof IEnergyContainer;
	}

	@Override
	public long doGetEnergy(Object obj, ForgeDirection direction)
	{
		return ((IEnergyContainer) obj).getEnergy(direction);
	}
}
