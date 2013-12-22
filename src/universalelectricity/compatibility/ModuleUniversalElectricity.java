package universalelectricity.compatibility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.item.IElectricalItem;

/** @author Calclavia */
public class ModuleUniversalElectricity extends CompatibilityModule
{
    @Override
    public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
    {
        return ((IEnergyInterface) obj).onReceiveEnergy(direction, energy, doReceive);
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return obj instanceof IEnergyInterface || obj instanceof IElectricalItem;
    }

    @Override
    public boolean doCanConnect(Object obj, ForgeDirection direction)
    {
        return ((IEnergyInterface) obj).canConnect(direction);
    }

    @Override
    public long doChargeItem(ItemStack itemStack, long joules, boolean doCharge)
    {
        if (itemStack != null && itemStack.getItem() instanceof IElectricalItem)
        {
            return ((IElectricalItem) itemStack.getItem()).recharge(itemStack, joules, doCharge);
        }
        return 0;
    }

    @Override
    public long doDischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
    {
        if (itemStack != null && itemStack.getItem() instanceof IElectricalItem)
        {
            return ((IElectricalItem) itemStack.getItem()).discharge(itemStack, joules, doDischarge);
        }
        return 0;
    }
}
