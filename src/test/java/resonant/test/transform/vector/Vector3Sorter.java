package resonant.test.transform.vector;

import junit.framework.TestCase;
import resonant.lib.transform.sorting.Vector3DistanceComparator;
import resonant.lib.transform.vector.Vector3;

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
        List<Vector3> list = new ArrayList();
        Vector3 vec_1 = newVector(list, 1, 0, 1);
        Vector3 vec_2 = newVector(list, 2, 0, 2);
        Vector3 vec_3 = newVector(list, 3, 0, 3);
        Vector3 vec_4 = newVector(list, -1, 0, -2);
        Vector3 vec_5 = newVector(list, -4, 0, -4);

        Collections.sort(list, new Vector3DistanceComparator(new Vector3(0, 0, 0)));
        assertEquals(list.get(0), vec_1);
        assertEquals(list.get(1), vec_4);
        assertEquals(list.get(2), vec_2);
        assertEquals(list.get(3), vec_3);
        assertEquals(list.get(4), vec_5);
    }

    private Vector3 newVector(List<Vector3> list, int x, int y, int z)
    {
        Vector3 vec = new Vector3(x, y, z);
        list.add(vec);
        return vec;
    }
}
