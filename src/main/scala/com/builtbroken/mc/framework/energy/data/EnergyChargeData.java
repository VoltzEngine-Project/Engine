package com.builtbroken.mc.framework.energy.data;

import com.builtbroken.mc.api.data.energy.IEnergyChargeData;

/**
 * Data object to store charge information about a device. Cna be used for anything (tiles, items, entities, etc). Designed
 * for use with JSON loading system to abstract logic.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/7/2017.
 */
public class EnergyChargeData implements IEnergyChargeData
{
    private int inputLimit;
    private int outputLimit;

    @Override
    public int getInputEnergyLimit()
    {
        return inputLimit;
    }

    @Override
    public int getOutputEnergyLimit()
    {
        return outputLimit;
    }

    public EnergyChargeData setInputEnergyLimit(int inputLimit)
    {
        this.inputLimit = inputLimit;
        if (inputLimit < 0)
        {
            this.inputLimit = -1;
        }
        return this;
    }

    public EnergyChargeData setOutputEnergyLimit(int outputLimit)
    {
        this.outputLimit = outputLimit;
        if (outputLimit < 0)
        {
            this.outputLimit = -1;
        }
        return this;
    }
}
