package resonant.test.transform.region;

import junit.framework.TestCase;
import resonant.lib.transform.region.Triangle;
import resonant.lib.transform.vector.Vector2;

/**
 * Created by robert on 12/17/2014.
 */
public class TriangleTest extends TestCase
{
    /**
     * Tests area method using 4 right triangles in each section
     * of pos & neg relations. It also tests the order of the points
     * when constructing the triangle to ensure there are no math errors
     */
    public void testRightTriangleAreas()
    {
        final double expectedArea = 2;
        //Using zero zero
        Vector2 zeroZero = new Vector2();
        Vector2 p1 = null;
        Vector2 p2 = null;
        Triangle a, b, c;

        for (int i = 0; i < 4; i++)
        {
            switch (i)
            {
                case 0:
                    //positive positive
                    p1 = new Vector2(0, 2);
                    p2 = new Vector2(2, 0);
                    break;
                case 2:
                    //neg positive
                    p1 = new Vector2(0, 2);
                    p2 = new Vector2(-2, 0);
                    break;
                case 3:
                    //positive neg
                    p1 = new Vector2(0, -2);
                    p2 = new Vector2(2, 0);
                    break;
                case 4:
                    //neg neg
                    p1 = new Vector2(0, -2);
                    p2 = new Vector2(-2, 0);
                    break;
            }

            a = new Triangle(zeroZero, p1, p2);
            b = new Triangle(p1, p2, zeroZero);
            c = new Triangle(p2, zeroZero, p1);

            assertEquals("Failed area check for A" + i, expectedArea, a.area());
            assertEquals("Failed area check for B" + i, expectedArea, b.area());
            assertEquals("Failed area check for C" + i, expectedArea, c.area());

        }

    }

    /**
     * Tests area method using 4 triangles in each section
     * of pos & neg relations. It also tests the order of the points
     * when constructing the triangle to ensure there are no math errors
     */
    public void testObtuseTriangleAreas()
    {
        final double expectedArea = 4;
        //Using zero zero
        Vector2 zeroZero = new Vector2();
        Vector2 p1 = null;
        Vector2 p2 = null;
        Triangle a, b, c;

        for (int i = 0; i < 4; i++)
        {
            switch (i)
            {
                case 0:
                    //UP
                    p1 = new Vector2(2, 2);
                    p2 = new Vector2(-2, 2);
                    break;
                case 2:
                    //DOWN
                    p1 = new Vector2(2, -2);
                    p2 = new Vector2(-2, -2);
                    break;
                case 3:
                    //RIGHT
                    p1 = new Vector2(2, 2);
                    p2 = new Vector2(2, -2);
                    break;
                case 4:
                    //LEFT
                    p1 = new Vector2(-2, 2);
                    p2 = new Vector2(-2, -2);
                    break;
            }

            a = new Triangle(zeroZero, p1, p2);
            b = new Triangle(p1, p2, zeroZero);
            c = new Triangle(p2, zeroZero, p1);

            assertEquals("Failed area check for A" + i, expectedArea, a.area());
            assertEquals("Failed area check for B" + i, expectedArea, b.area());
            assertEquals("Failed area check for C" + i, expectedArea, c.area());

        }

    }
}
