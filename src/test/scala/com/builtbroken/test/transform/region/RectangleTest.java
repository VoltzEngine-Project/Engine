package com.builtbroken.test.transform.region;

import com.builtbroken.mc.lib.transform.vector.Point;
import junit.framework.TestCase;
import com.builtbroken.mc.lib.transform.region.Rectangle;
import com.builtbroken.mc.lib.transform.region.Triangle;

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
        Rectangle rect = new Rectangle(new Point(), new Point());
        assertEquals("Expected zero as both corners are zero zero", 0.0, rect.getArea());
        rect = new Rectangle(new Point(0, 1), new Point(0, 2));
        assertEquals("Expected zero as both corners are in a strait line", 0.0, rect.getArea());

        //Random are checks
        rect = new Rectangle(new Point(0, 0), new Point(2, 2));
        assertEquals("Expected an exact match for area check one", 4.0, rect.getArea());

        rect = new Rectangle(new Point(0, 0), new Point(-2, -2));
        assertEquals("Expected an exact match for area check two", 4.0, rect.getArea());

        rect = new Rectangle(new Point(-2, -2), new Point(2, 2));
        assertEquals("Expected an exact match for area check three", 16.0, rect.getArea());

        rect = new Rectangle(new Point(10, 20), new Point(-20, -10));
        assertEquals("Expected an exact match for area check four", 900.0, rect.getArea());
    }

    /**
     * Checks if the basic version of the point bounding box check is working
     */
    public void testIsWithin()
    {
        Rectangle rect = new Rectangle(new Point(0, 0), new Point(2, 2));

        List<Point> points_inside = new LinkedList();
        points_inside.add(rect.cornerA());
        points_inside.add(rect.cornerB());
        points_inside.add(rect.cornerC());
        points_inside.add(rect.cornerD());
        points_inside.add(new Point(1, 1));

        List<Point> points_outside = new LinkedList();
        points_outside.add(new Point(-1, -1));
        points_outside.add(new Point(0, -1));
        points_outside.add(new Point(-1, 0));
        points_outside.add(new Point(3, 0));
        points_outside.add(new Point(0, 3));

        for (int i = 0; i < 4; i++)
        {
            //First two runs are inside checks
            boolean inside = i <= 1;
            boolean rotated = (i == 1 || i == 3);
            List<Point> l = inside ? points_inside : points_outside;

            for (Point vec : l)
            {
                boolean flag = !rotated ? rect.isWithin(vec) : rect.isWithin_rotated(vec);

                //Debug for when the checks fail and we need to know why
                if (flag != inside)
                {
                    System.out.println("===   Debug   ===");
                    if (!rotated)
                    {
                        System.out.println("isWithinX: " + (vec.x() >= rect.min().x() && vec.x() <= rect.max().x()));
                        System.out.println("isWithinY: " + (vec.y() >= rect.min().y() && vec.y() <= rect.max().y()));
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
