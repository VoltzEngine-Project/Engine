package resonant.test.transform.region;

import junit.framework.TestCase;
import resonant.lib.transform.region.Rectangle;
import resonant.lib.transform.vector.Vector2;

/**
 * Created by robert on 12/17/2014.
 */
public class RectangleTest extends TestCase
{
    public void testArea()
    {
        //For the hell of it
        Rectangle rect = new Rectangle(new Vector2(), new Vector2());
        assertEquals("Expected zero as both corners are zero zero", 0.0, rect.area());
        rect = new Rectangle(new Vector2(0, 1), new Vector2(0, 2));
        assertEquals("Expected zero as both corners are in a strait line", 0.0, rect.area());

        //Random are checks
        rect = new Rectangle(new Vector2(0, 0), new Vector2(2, 2));
        assertEquals("Expected an exact match for area check one", 4.0, rect.area());

        rect = new Rectangle(new Vector2(0, 0), new Vector2(-2, -2));
        assertEquals("Expected an exact match for area check two", 4.0, rect.area());

        rect = new Rectangle(new Vector2(-2, -2), new Vector2(2, 2));
        assertEquals("Expected an exact match for area check three", 16.0, rect.area());

        rect = new Rectangle(new Vector2(10, 20), new Vector2(-20, -10));
        assertEquals("Expected an exact match for area check four", 900.0, rect.area());
    }

    public void isPointInSideArea()
    {
        Rectangle rect = new Rectangle(new Vector2(0, 0), new Vector2(2, 2));

        //Always check the zero case :)
        Vector2 p1 = new Vector2();
        assertEquals("Failed on (0,0) corner check", true, rect.isWithin(p1));

        p1 = new Vector2(2, 2);
        assertEquals("Failed on (2, 2) corner check", true, rect.isWithin(p1));

        p1 = new Vector2(0, 2);
        assertEquals("Failed on (0, 2) corner check", true, rect.isWithin(p1));

        p1 = new Vector2(2, 0);
        assertEquals("Failed on (2, 0) corner check", true, rect.isWithin(p1));

        //Points inside
        p1 = new Vector2(1, 1);
        assertEquals("Failed on (1, 1) check", true, rect.isWithin(p1));

        //Points outside
        p1 = new Vector2(-1, -1);
        assertEquals("Failed on (-1, -1) corner check", false, rect.isWithin(p1));

        p1 = new Vector2(0, -1);
        assertEquals("Failed on (0, -1) corner check", false, rect.isWithin(p1));

        p1 = new Vector2(-1, 0);
        assertEquals("Failed on (-1, 0) corner check", false, rect.isWithin(p1));

        p1 = new Vector2(3, 0);
        assertEquals("Failed on (3, 0) corner check", false, rect.isWithin(p1));

        p1 = new Vector2(0, 3);
        assertEquals("Failed on (0, 3) corner check", false, rect.isWithin(p1));

    }
}
