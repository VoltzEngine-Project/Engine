package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.edit.IWorldEdit;

import java.util.Comparator;

/**
 * Comparator for sorting collections of IWorldEdit objects based on tier position data only. Sorts based on greater and less then comparisons, or from center of the world.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/14/2015.
 */
public class WorldEditSorter implements Comparator<IWorldEdit>
{
    @Override
    public int compare(IWorldEdit o1, IWorldEdit o2)
    {
        if (o1.world().provider.dimensionId < o2.world().provider.dimensionId)
        {
            return -1;
        }
        else if (o1.world().provider.dimensionId > o2.world().provider.dimensionId)
        {
            return 1;
        }
        else if (o1.x() < o2.x())
        {
            return -1;
        }
        else if (o1.x() > o2.x())
        {
            return 1;
        }
        else if (o1.y() < o2.y())
        {
            return -1;
        }
        else if (o1.y() > o2.y())
        {
            return 1;
        }
        else if (o1.z() < o2.z())
        {
            return -1;
        }
        else if (o1.z() > o2.z())
        {
            return 1;
        }
        return 0;
    }
}