package universalelectricity.compatibility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.grid.INodeProvider;
import universalelectricity.api.grid.electric.IElectricNode;
import universalelectricity.api.grid.electric.IEnergyContainer;
import universalelectricity.api.item.IEnergyItem;

/**
 * @author Calclavia
 */
public class ModuleUniversalElectricity extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object handler, ForgeDirection direction, long energy, boolean doReceive)
	{
		((INodeProvider) handler).getNode(IElectricNode.class, direction).applyPower(energy);
		return energy;
	}

	@Override
	public long doExtractEnergy(Object handler, ForgeDirection direction, long energy, boolean doExtract)
	{
		((INodeProvider) handler).getNode(IElectricNode.class, direction).drawPower(energy);
		return energy;
	}

	@Override
	public boolean doIsHandler(Object obj)
	{
		return (obj instanceof INodeProvider && ((INodeProvider) obj).getNode(IElectricNode.class, null) != null) || obj instanceof IEnergyItem;
	}

	@Override
	public boolean doCanConnect(Object obj, ForgeDirection direction, Object source)
	{
		return ((INodeProvider) obj).getNode(IElectricNode.class, direction) != null;
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

	@Override
	public long doGetMaxEnergy(Object obj, ForgeDirection direction)
	{
		return ((IEnergyContainer) obj).getEnergyCapacity(direction);
	}

	@Override
	public long doGetEnergyItem(ItemStack is)
	{
		return ((IEnergyItem) is.getItem()).getEnergy(is);
	}

	@Override
	public long doGetMaxEnergyItem(ItemStack is)
	{
		return ((IEnergyItem) is.getItem()).getEnergyCapacity(is);
	}
}
