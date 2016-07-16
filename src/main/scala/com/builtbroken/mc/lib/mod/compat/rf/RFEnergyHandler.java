package com.builtbroken.mc.lib.mod.compat.rf;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;
import com.builtbroken.mc.lib.energy.EnergyHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Compatibility with RF energy system
 * Created by Robert on 8/10/2015.
 */
public class RFEnergyHandler extends EnergyHandler
{
    public static double TO_RF_FROM_UE;
    public static double TO_UE_FROM_RF;

    //TODO ensure that ratio does come with a loss in energy due to rounding errors
    public RFEnergyHandler(double ratio)
    {
        super("rf", "flux", "rf", ratio);
        TO_RF_FROM_UE = toForgienEnergy;
        TO_UE_FROM_RF = toUEEnergy;
        //TODO load handler even if TE or it's core is not loaded
        //Instead check that the 4 API files used exist then load so we support any mod that uses RF
    }

    @Override
    public double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive)
    {
        if (handler instanceof IEnergyConnection && ((IEnergyConnection) handler).canConnectEnergy(direction))
        {
            if (handler instanceof IEnergyHandler)
            {
                return ((IEnergyHandler) handler).receiveEnergy(direction, (int) (energy * toForgienEnergy), doReceive) * toUEEnergy;
            }
            else if (handler instanceof IEnergyStorage)
            {
                return ((IEnergyStorage) handler).receiveEnergy((int) (energy * toForgienEnergy), doReceive) * toUEEnergy;
            }
        }
        return 0;
    }

    @Override
    public double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract)
    {
        if (handler instanceof IEnergyConnection && ((IEnergyConnection) handler).canConnectEnergy(direction))
        {
            if (handler instanceof IEnergyHandler)
            {
                return ((IEnergyHandler) handler).extractEnergy(direction, (int) (energy * toForgienEnergy), doExtract) * toUEEnergy;
            }
            else if (handler instanceof IEnergyStorage)
            {
                return ((IEnergyStorage) handler).extractEnergy((int) (energy * toForgienEnergy), doExtract) * toUEEnergy;
            }
        }
        return 0;
    }

    @Override
    public boolean doIsHandler(Object obj, ForgeDirection dir)
    {
        return obj instanceof IEnergyStorage || obj instanceof IEnergyHandler;
    }

    @Override
    public boolean doIsEnergyContainer(Object obj)
    {
        return obj instanceof IEnergyStorage;
    }

    @Override
    public boolean canConnect(Object obj, ForgeDirection direction, Object source)
    {
        return obj instanceof IEnergyConnection ? ((IEnergyConnection) obj).canConnectEnergy(direction) : doIsEnergyContainer(obj);
    }

    @Override
    public double getEnergy(Object obj, ForgeDirection direction)
    {
        return obj instanceof IEnergyStorage ? ((IEnergyStorage) obj).getEnergyStored() * toUEEnergy : 0;
    }

    @Override
    public double getMaxEnergy(Object obj, ForgeDirection direction)
    {
        return obj instanceof IEnergyStorage ? ((IEnergyStorage) obj).getMaxEnergyStored() * toUEEnergy : 0;
    }

    @Override
    public double chargeItem(ItemStack is, double joules, boolean docharge)
    {
        if (is != null && is.getItem() instanceof IEnergyContainerItem)
        {
            return ((IEnergyContainerItem) is.getItem()).receiveEnergy(is, (int) (joules * toForgienEnergy), docharge) * toUEEnergy;
        }
        return 0;
    }

    @Override
    public double dischargeItem(ItemStack is, double joules, boolean doDischarge)
    {
        if (is != null && is.getItem() instanceof IEnergyContainerItem)
        {
            return ((IEnergyContainerItem) is.getItem()).extractEnergy(is, (int) (joules * toForgienEnergy), doDischarge) * toUEEnergy;
        }
        return 0;
    }

    @Override
    public ItemStack getItemWithCharge(ItemStack itemStack, double energy)
    {
        chargeItem(itemStack, (energy * toForgienEnergy), true);
        return itemStack;
    }

    @Override
    public double getEnergyItem(ItemStack is)
    {
        return is != null && is.getItem() instanceof IEnergyContainerItem ? ((IEnergyContainerItem) is.getItem()).getEnergyStored(is) * toUEEnergy : 0;
    }

    @Override
    public double getMaxEnergyItem(ItemStack is)
    {
        return is != null && is.getItem() instanceof IEnergyContainerItem ? ((IEnergyContainerItem) is.getItem()).getMaxEnergyStored(is) * toUEEnergy : 0;
    }
}
