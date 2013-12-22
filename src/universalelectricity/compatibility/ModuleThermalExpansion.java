package universalelectricity.compatibility;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.CompatibilityModule;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

/** @author Calclavia */
public class ModuleThermalExpansion extends CompatibilityModule
{
    @Override
    public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
    {
        return (long) (((IEnergyHandler) obj).receiveEnergy(direction, (int) (energy * CompatibilityType.THERMAL_EXPANSION.ratio), !doReceive) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return obj instanceof IEnergyHandler;
    }

    @Override
    public boolean doCanConnect(Object obj, ForgeDirection direction)
    {
        return ((IEnergyHandler) obj).canInterface(direction);
    }

    @Override
    public long chargeItem(ItemStack itemStack, long joules, boolean docharge)
    {
        if (itemStack.getItem() instanceof IEnergyContainerItem)
        {
            return (long) (((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, (int) (joules * CompatibilityType.THERMAL_EXPANSION.ratio), docharge) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public long dischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
    {
        if (itemStack.getItem() instanceof IEnergyContainerItem)
        {
            return (long) (((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) (joules * CompatibilityType.THERMAL_EXPANSION.ratio), doDischarge) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public boolean doIsEnergyItem(Item item)
    {
        return item instanceof IEnergyContainerItem;
    }
}
