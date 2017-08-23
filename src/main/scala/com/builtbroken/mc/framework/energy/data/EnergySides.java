package com.builtbroken.mc.framework.energy.data;

import com.builtbroken.mc.data.Direction;

/**
 * Quick way to store exact power values for a machine per side. Mainly used as
 * a more accurate return type then a total number for the machine.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/21/2016.
 */
public class EnergySides
{
    private final double[] power = new double[7];

    public void set(Direction side, double power)
    {
        this.power[side.ordinal()] = power;
    }

    public double get(Direction side)
    {
        return power[side.ordinal()];
    }

    public double DOWN()
    {
        return power[0];
    }

    public double UP()
    {
        return power[1];
    }

    public double NORTH()
    {
        return power[2];
    }

    public double SOUTH()
    {
        return power[3];
    }

    public double WEST()
    {
        return power[4];
    }

    public double EAST()
    {
        return power[5];
    }
}
