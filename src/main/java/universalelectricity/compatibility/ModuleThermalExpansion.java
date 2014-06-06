package universalelectricity.compatibility;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.CompatibilityType;

/** @author Calclavia */
public class ModuleThermalExpansion extends CompatibilityModule
{
    @Override
    public long doReceiveEnergy(Object handler, ForgeDirection direction, long energy, boolean doReceive)
    {
        if (energy > 0)
        {
            double rf = energy * CompatibilityType.THERMAL_EXPANSION.ratio;
            if (rf > 0 && rf <= 1)
            {
                rf = 1;
            }
            return (long) (((IEnergyHandler) handler).receiveEnergy(direction, (int) rf, !doReceive) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public long doExtractEnergy(Object handler, ForgeDirection direction, long energy, boolean doExtract)
    {
        if (energy > 0)
        {
            double rf = energy * CompatibilityType.THERMAL_EXPANSION.ratio;
            if (rf > 0 && rf <= 1)
            {
                rf = 1;
            }
            return (long) (((IEnergyHandler) handler).extractEnergy(direction, (int) rf, !doExtract) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return obj instanceof IEnergyHandler || obj instanceof IEnergyContainerItem;
    }

    @Override
    public boolean doCanConnect(Object obj, ForgeDirection direction, Object source)
    {
        return ((IEnergyHandler) obj).canInterface(direction);
    }

    @Override
    public long doChargeItem(ItemStack itemStack, long joules, boolean docharge)
    {
        if (itemStack.getItem() instanceof IEnergyContainerItem)
        {
            return (long) Math.floor(((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, (int) Math.ceil(joules * CompatibilityType.THERMAL_EXPANSION.ratio), !docharge) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public long doDischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
    {
        if (itemStack.getItem() instanceof IEnergyContainerItem)
        {
            return (long) Math.floor(((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.ceil(joules * CompatibilityType.THERMAL_EXPANSION.ratio), !doDischarge) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public ItemStack doGetItemWithCharge(ItemStack is, long energy)
    {
        return null;
    }

    @Override
    public boolean doIsEnergyContainer(Object obj)
    {
        return obj instanceof IEnergyHandler;
    }

    @Override
    public long doGetEnergy(Object obj, ForgeDirection from)
    {
        return (long) (((IEnergyHandler) obj).getEnergyStored(from) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
    }

    @Override
    public long doGetMaxEnergy(Object obj, ForgeDirection from)
    {
        return (long) (((IEnergyHandler) obj).getMaxEnergyStored(from) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
    }

    @Override
    public long doGetEnergyItem(ItemStack is)
    {
        if (is.getItem() instanceof IEnergyContainerItem)
        {
            return (long) (((IEnergyContainerItem) is.getItem()).getEnergyStored(is) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public long doGetMaxEnergyItem(ItemStack is)
    {
        if (is.getItem() instanceof IEnergyContainerItem)
        {
            return (long) (((IEnergyContainerItem) is.getItem()).getMaxEnergyStored(is) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
        }
        return 0;
    }
}
