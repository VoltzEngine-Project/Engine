package universalelectricity.core.item;

import net.minecraft.item.ItemStack;
import thermalexpansion.api.item.IChargeableItem;

public interface IItemElectric extends IChargeableItem, IItemVoltage
{
	public void setEnergy(ItemStack itemStack, double joules);
}
