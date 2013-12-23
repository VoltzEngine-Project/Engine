package universalelectricity.core.asm.template;

import universalelectricity.api.item.IEnergyItem;
import net.minecraft.item.ItemStack;

/**
 * @author Calclavia
 * 
 */
public class StaticItemForwarder
{
	public static long recharge(IEnergyItem energyItem, ItemStack itemStack, long energy, boolean doRecharge)
	{
		return energyItem.recharge(itemStack, energy, doRecharge);
	}

	public static long discharge(IEnergyItem energyItem, ItemStack itemStack, long energy, boolean doDischarge)
	{
		return energyItem.discharge(itemStack, energy, doDischarge);
	}

	public static long getEnergy(IEnergyItem energyItem, ItemStack itemStack)
	{
		return energyItem.getEnergy(itemStack);
	}

	public static long getEnergyCapacity(IEnergyItem energyItem, ItemStack itemStack)
	{
		return energyItem.getEnergyCapacity(itemStack);
	}
}
