package resonant.test.transform.region;

import junit.framework.TestCase;
import resonant.lib.transform.region.Rectangle;
import resonant.lib.transform.region.Triangle;
import resonant.lib.transform.vector.Vector2;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by robert on 12/17/2014.
 */
public class RectangleTest extends TestCase
{
    /**
     * Checks too see if the area math is working :P
     */
    public void testArea()
    {
        //For the hell of it
        Rectangle rect = new Rectangle(new Vector2(), new Vector2());
        assertEquals("Expected zero as both corners are zero zero", 0.0, rect.getArea());
        rect = new Rectangle(new Vector2(0, 1), new Vector2(0, 2));
        assertEquals("Expected zero as both corners are in a strait line", 0.0, rect.getArea());

        //Random are checks
        rect = new Rectangle(new Vector2(0, 0), new Vector2(2, 2));
        assertEquals("Expected an exact match for area check one", 4.0, rect.getArea());

        rect = new Rectangle(new Vector2(0, 0), new Vector2(-2, -2));
        assertEquals("Expected an exact match for area check two", 4.0, rect.getArea());

        rect = new Rectangle(new Vector2(-2, -2), new Vector2(2, 2));
        assertEquals("Expected an exact match for area check three", 16.0, rect.getArea());

        rect = new Rectangle(new Vector2(10, 20), new Vector2(-20, -10));
        assertEquals("Expected an exact match for area check four", 900.0, rect.getArea());
    }

    /**
     * Checks if the basic version of the point bounding box check is working
     */
    public void testIsWithin()
    {
        Rectangle rect = new Rectangle(new Vector2(0, 0), new Vector2(2, 2));

        List<Vector2> points_inside = new LinkedList();
        points_inside.add(rect.cornerA());
        points_inside.add(rect.cornerB());
        points_inside.add(rect.cornerC());
        points_inside.add(rect.cornerD());
        points_inside.add(new Vector2(1, 1));

        List<Vector2> points_outside = new LinkedList();
        points_outside.add(new Vector2(-1, -1));
        points_outside.add(new Vector2(0, -1));
        points_outside.add(new Vector2(-1, 0));
        points_outside.add(new Vector2(3, 0));
        points_outside.add(new Vector2(0, 3));

        for (int i = 0; i < 4; i++)
        {
            //First two runs are inside checks
            boolean inside = i <= 1;
            boolean rotated = (i == 1 || i == 3);
            List<Vector2> l = inside ? points_inside : points_outside;

            for (Vector2 vec : l)
            {
                boolean flag = !rotated ? rect.isWithin(vec) : rect.isWithin_rotated(vec);

                //Debug for when the checks fail and we need to know why
                if (flag != inside)
                {
                    System.out.println("===   Debug   ===");
                    if (!rotated)
                    {
                        System.out.println("isWithinX: " + rect.isWithinX(vec));
                        System.out.println("isWithinY: " + rect.isWithinX(vec));
                    }
                    else
                    {
                        double ab = new Triangle(rect.cornerA(), rect.cornerB(), vec).getArea();
                        double bc = new Triangle(rect.cornerB(), rect.cornerC(), vec).getArea();
                        double cd = new Triangle(rect.cornerC(), rect.cornerD(), vec).getArea();
                        double da = new Triangle(rect.cornerD(), rect.cornerA(), vec).getArea();
                        System.out.println("TriABP Area: " + ab);
                        System.out.println("TriBCP Area: " + bc);
                        System.out.println("TriCBP Area: " + cd);
                        System.out.println("TriDAP Area: " + da);
                        System.out.println("Total Area:  " + (ab + bc + cd +da));
                        System.out.println("Rect Area:   " + rect.getArea());
                    }
                    System.out.println("==================");

                    //Failure message
                    String msg = "Failed for ";
                    msg += (!rotated ? "Normal Test " : "Rotated Test ");
                    msg += vec +" ";
                    msg += (inside ? " should be inside the rect!" : " should be outside the rect!");
                    fail(msg);
                }
            }
        }
    }
}
