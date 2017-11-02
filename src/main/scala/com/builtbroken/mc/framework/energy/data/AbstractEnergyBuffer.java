package com.builtbroken.mc.framework.energy.data;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.framework.energy.UniversalEnergySystem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implementation of {@link IEnergyBuffer} to be used as a base for creating energy buffer objects.
 * Use {@link EnergyBuffer} for a completed version.
 * <p>
 * This exists seperate from {@link EnergyBuffer} to allow shared implementation while
 * allow control over {@link #getMaxBufferSize()} which {@link EnergyBuffer} does not allow.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/2/2017.
 */
public abstract class AbstractEnergyBuffer implements IEnergyBuffer
{
    private int energyStorage;

    @Override
    public int addEnergyToStorage(int energy, boolean doAction)
    {
        int prev = getEnergyStored();
        if (energy > 0)
        {
            int roomLeft = getMaxBufferSize() - getEnergyStored();
            if (energy < roomLeft)
            {
                if (doAction)
                {
                    energyStorage += energy;
                    if (prev != energyStorage)
                    {
                        onPowerChange(prev, getEnergyStored(), EnergyBuffer.EnergyActionType.ADD);
                    }
                }
                return energy;
            }
            else
            {
                if (doAction)
                {
                    energyStorage = getMaxBufferSize();
                    if (prev != energyStorage)
                    {
                        onPowerChange(prev, getEnergyStored(), EnergyBuffer.EnergyActionType.ADD);
                    }
                }
                return roomLeft;
            }
        }
        return 0;
    }

    @Override
    public int removeEnergyFromStorage(int energy, boolean doAction)
    {
        int prev = getEnergyStored();
        if (energy > 0 && getEnergyStored() > 0)
        {
            if (energy >= getEnergyStored())
            {
                if (doAction)
                {
                    energyStorage = 0;
                    if (prev != getEnergyStored())
                    {
                        onPowerChange(prev, getEnergyStored(), EnergyBuffer.EnergyActionType.REMOVE);
                    }
                }
                return getMaxBufferSize();
            }
            else
            {
                if (doAction)
                {
                    energyStorage -= energy;
                    if (prev != getEnergyStored())
                    {
                        onPowerChange(prev, getEnergyStored(), EnergyBuffer.EnergyActionType.REMOVE);
                    }
                }
                return energy;
            }
        }
        return 0;
    }

    /**
     * Called when the power changes in the buffer
     *
     * @param prevEnergy - energy before action
     * @param current    - energy after action
     */
    protected void onPowerChange(int prevEnergy, int current, EnergyBuffer.EnergyActionType actionType)
    {

    }

    @Override
    public int getEnergyStored()
    {
        return energyStorage;
    }

    @Override
    public void setEnergyStored(int energy)
    {
        int prev = getEnergyStored();
        this.energyStorage = Math.min(getMaxBufferSize(), Math.max(0, energy));
        if (prev != energyStorage)
        {
            onPowerChange(prev, getEnergyStored(), EnergyBuffer.EnergyActionType.SET);
        }
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

    public static enum EnergyActionType
    {
        ADD,
        REMOVE,
        SET
    }
}
