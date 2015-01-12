package com.builtbroken.mc.lib.transform.sorting;

import com.builtbroken.mc.lib.transform.vector.Pos;

import java.util.Comparator;

public class Vector3DistanceComparator implements Comparator<Pos>
{
    final Pos center;
    final boolean closest;

    public Vector3DistanceComparator(Pos center)
    {
       this(center, true);
    }

    public Vector3DistanceComparator(Pos center, boolean closest)
    {
        this.center = center;
        this.closest = closest;
    }

    @Override
    public int compare(Pos o1, Pos o2)
    {
        double d = o1.distance(center);
        double d2 = o2.distance(center);
        return d > d2 ? 1 : d == d2 ? 0 : -1;
    }
}