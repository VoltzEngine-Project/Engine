package com.builtbroken.mc.prefab.energy;

import com.builtbroken.mc.api.energy.IEnergyBuffer;

/**
 * Created by Dark on 8/15/2015.
 */
public class EnergyBuffer implements IEnergyBuffer
{
    private final int maxStorage;
    private int energyStorage;

    public EnergyBuffer(int maxStorage)
    {
        this.maxStorage = maxStorage;
    }

    @Override
    public int addEnergyToStorage(int energy, boolean doAction)
    {
        if (energy > 0)
        {
            int roomLeft = getMaxBufferSize() - getEnergyStored();
            if (energy < roomLeft)
            {
                if (doAction)
                    energyStorage += energy;
                return energy;
            }
            else
            {
                if (doAction)
                    energyStorage = getMaxBufferSize();
                return roomLeft;
            }
        }
        return 0;
    }

    @Override
    public int removeEnergyFromStorage(int energy, boolean doAction)
    {
        if (energy > 0)
        {
            if (energy >= maxStorage)
            {
                if (doAction)
                    energyStorage = 0;
                return maxStorage;
            }
            else
            {
                if (doAction)
                    energyStorage -= energy;
                return energy;
            }
        }
        return 0;
    }

    @Override
    public int getMaxBufferSize()
    {
        return maxStorage;
    }

    @Override
    public int getEnergyStored()
    {
        return energyStorage;
    }
}
