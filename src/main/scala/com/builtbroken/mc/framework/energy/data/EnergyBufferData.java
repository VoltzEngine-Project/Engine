package com.builtbroken.mc.framework.energy.data;

import com.builtbroken.mc.api.data.energy.IEnergyBufferData;

/**
 * Used to store information about energy buffers
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/7/2017.
 */
public class EnergyBufferData implements IEnergyBufferData
{
    private int power;

    @Override
    public int getEnergyCapacity()
    {
        return power;
    }

    public EnergyBufferData setEnergyCapacity(int power)
    {
        this.power = Math.max(0, power);
        return this;
    }
}
