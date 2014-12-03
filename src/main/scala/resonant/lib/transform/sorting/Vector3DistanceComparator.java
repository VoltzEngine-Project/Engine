package resonant.lib.transform.sorting;

import resonant.lib.transform.vector.Vector3;

import java.util.Comparator;

public class Vector3DistanceComparator implements Comparator<Vector3>
{
    final Vector3 center;
    final boolean closest;

    public Vector3DistanceComparator(Vector3 center)
    {
       this(center, true);
    }

    public Vector3DistanceComparator(Vector3 center, boolean closest)
    {
        this.center = center;
        this.closest = closest;
    }

    @Override
    public int compare(Vector3 o1, Vector3 o2)
    {
        double d = o1.distance(center);
        double d2 = o2.distance(center);
        return d > d2 ? 1 : d == d2 ? 0 : -1;
    }
}