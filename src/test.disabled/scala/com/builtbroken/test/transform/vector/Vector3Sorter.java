package com.builtbroken.test.transform.vector;

import junit.framework.TestCase;
import com.builtbroken.mc.imp.transform.sorting.Vector3DistanceComparator;
import com.builtbroken.mc.imp.transform.vector.Pos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by robert on 12/3/2014.
 */
public class Vector3Sorter extends TestCase
{
    public void testClosestSorter()
    {
        List<Pos> list = new ArrayList();
        Pos vec_1 = newVector(list, 1, 0, 1);
        Pos vec_2 = newVector(list, 2, 0, 2);
        Pos vec_3 = newVector(list, 3, 0, 3);
        Pos vec_4 = newVector(list, -1, 0, -2);
        Pos vec_5 = newVector(list, -4, 0, -4);

        Collections.sort(list, new Vector3DistanceComparator(new Pos(0, 0, 0)));
        assertEquals(list.get(0), vec_1);
        assertEquals(list.get(1), vec_4);
        assertEquals(list.get(2), vec_2);
        assertEquals(list.get(3), vec_3);
        assertEquals(list.get(4), vec_5);
    }

    private Pos newVector(List<Pos> list, int x, int y, int z)
    {
        Pos vec = new Pos(x, y, z);
        list.add(vec);
        return vec;
    }
}
