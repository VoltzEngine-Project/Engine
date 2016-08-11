package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.lib.energy.EnergyHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public class ICHandler extends EnergyHandler
{
    public ICHandler()
    {
        super("IndustrialCraft", "Energy Unit", "eu", 10);
    }

    @Override
    public double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive)
    {
        return 0;
    }

    @Override
    public double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract)
    {
        return 0;
    }

    @Override
    public double chargeItem(ItemStack itemStack, double joules, boolean docharge)
    {
        return 0;
    }

    @Override
    public double dischargeItem(ItemStack itemStack, double joules, boolean doDischarge)
    {
        return 0;
    }

    @Override
    public boolean doIsHandler(Object obj, ForgeDirection dir)
    {
        return false;
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return false;
    }

    @Override
    public boolean doIsEnergyContainer(Object obj)
    {
        return false;
    }

    @Override
    public boolean canConnect(Object obj, ForgeDirection direction, Object source)
    {
        return false;
    }

    @Override
    public ItemStack getItemWithCharge(ItemStack itemStack, double energy)
    {
        return null;
    }

    @Override
    public double getEnergy(Object obj, ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public double getMaxEnergy(Object handler, ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public double getEnergyItem(ItemStack is)
    {
        return 0;
    }

    @Override
    public double getMaxEnergyItem(ItemStack is)
    {
        return 0;
    }

    @Override
    public double clearEnergy(Object handler, boolean doAction)
    {
        return 0;
    }
}
