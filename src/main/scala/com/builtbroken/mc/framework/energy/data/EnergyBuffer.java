package com.builtbroken.mc.framework.energy.data;

/**
 * Basic implementation of energy buffer
 * Created by Dark on 8/15/2015.
 */
public class EnergyBuffer extends AbstractEnergyBuffer
{
    private final int maxBufferSize;

    public EnergyBuffer(int maxStorage)
    {
        this.maxBufferSize = maxStorage;
    }

    @Override
    public int getMaxBufferSize()
    {
        return maxBufferSize;
    }
}
