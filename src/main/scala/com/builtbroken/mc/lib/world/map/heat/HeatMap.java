package com.builtbroken.mc.lib.world.map.heat;

/**
 * Tracks heat energy values for the world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on  2/25/2015.
 */
public class HeatMap
{
    public final int dimID;

    public HeatMap(int dimID)
    {
        this.dimID = dimID;
    }

    /**
     * Dimension ID this map tracks
     *
     * @return valid dim ID.
     */
    public int dimID()
    {
        return dimID;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof HeatMap)
        {
            return ((HeatMap) object).dimID == dimID;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "RadiationMap[" + dimID + "]";
    }
}
