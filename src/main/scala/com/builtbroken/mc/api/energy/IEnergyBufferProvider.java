package com.builtbroken.mc.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Dark on 8/15/2015.
 */
public interface IEnergyBufferProvider
{
    IEnergyBuffer getEnergyBuffer(ForgeDirection side);
}
