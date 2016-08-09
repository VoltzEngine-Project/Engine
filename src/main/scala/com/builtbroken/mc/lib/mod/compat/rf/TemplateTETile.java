package com.builtbroken.mc.lib.mod.compat.rf;

import cofh.api.energy.IEnergyHandler;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.lib.energy.UniversalEnergySystem;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * An ASM template used to transform classes containing {@link com.builtbroken.mc.api.InjectTemplate}
 *
 * @author Calclavia, DarkGuardsman
 */
public abstract class TemplateTETile implements IEnergyHandler, IEnergyBufferProvider
{
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        //Static forward is used to prevent ASM errors
        return StaticForwarder.receiveEnergy(this, from, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return StaticForwarder.extractEnergy(this, from, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return StaticForwarder.getEnergyStored(this, from);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return StaticForwarder.getMaxEnergyStored(this, from);
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return UniversalEnergySystem.canConnect(this, from, this);
    }
}
