package com.builtbroken.mc.lib.mod.compat.rf;

import cofh.api.energy.IEnergyHandler;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
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
        return RFStaticForwarder.receiveEnergy(this, from, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return RFStaticForwarder.extractEnergy(this, from, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return RFStaticForwarder.getEnergyStored(this, from);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return RFStaticForwarder.getMaxEnergyStored(this, from);
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return true; //TODO add connection interface
    }
}
