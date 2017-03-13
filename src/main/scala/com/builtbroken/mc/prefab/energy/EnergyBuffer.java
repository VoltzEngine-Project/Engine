package com.builtbroken.mc.prefab.energy;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.lib.energy.UniversalEnergySystem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Basic implementation of energy buffer
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
                {
                    energyStorage += energy;
                }
                return energy;
            }
            else
            {
                if (doAction)
                {
                    energyStorage = getMaxBufferSize();
                }
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
                {
                    energyStorage = 0;
                }
                return maxStorage;
            }
            else
            {
                if (doAction)
                {
                    energyStorage -= energy;
                }
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

    /**
     * Called to remove energy from the item and add it to this storage
     * <p>
     * Helper method to simplify the need to call {@link UniversalEnergySystem} directly
     *
     * @param stackInSlot - stack
     */
    public void addEmeryFromItem(ItemStack stackInSlot)
    {
        if (UniversalEnergySystem.isHandler(stackInSlot, ForgeDirection.UNKNOWN))
        {
            int energy = (int) Math.floor(UniversalEnergySystem.drain(stackInSlot, Integer.MAX_VALUE, false));
            if (energy > 0)
            {
                UniversalEnergySystem.drain(stackInSlot, addEnergyToStorage(energy, true), true);
            }
        }
    }
}
