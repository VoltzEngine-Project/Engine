package universalelectricity.core.asm.template.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.item.IEnergyItem;
import cofh.api.energy.IEnergyContainerItem;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class TemplateTEItem extends Item implements IEnergyContainerItem, IEnergyItem
{
	public TemplateTEItem(int par1)
	{
		super(par1);
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
	{
		return (int) (StaticItemForwarder.recharge(this, container, (long) (maxReceive * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio), !simulate) * CompatibilityType.THERMAL_EXPANSION.ratio);
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{
		return (int) (StaticItemForwarder.discharge(this, container, (long) (maxExtract * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio), !simulate) * CompatibilityType.THERMAL_EXPANSION.ratio);
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
		return (int) (StaticItemForwarder.getEnergy(this, container) * CompatibilityType.THERMAL_EXPANSION.ratio);
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return (int) (StaticItemForwarder.getEnergyCapacity(this, container) * CompatibilityType.THERMAL_EXPANSION.ratio);
	}

}
