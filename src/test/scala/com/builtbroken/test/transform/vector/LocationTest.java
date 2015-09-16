package com.builtbroken.test.transform.vector;

import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.world.World;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Test for {@link Location}
 * Created by robert on 4/25/2015.
 */
@RunWith(VoltzTestRunner.class)
public class LocationTest extends AbstractTest
{
    @Test
    public void testEquals()
    {
        World world = FakeWorld.newWorld("LocationTest");
        World world2 = FakeWorld.newWorld("LocationTest2");

        Location location = new Location(world, 0, 0, 0);
        Location location2 = new Location(world, 0, 0, 0);

        //Test would equals
        Assert.assertEquals("Location one should equal itself", location, location);
        Assert.assertEquals("Location two should equal itself",location2, location2);
        Assert.assertEquals("Location one & two should equal each other",location, location2);

        location2 = new Location(world2, 0, 0, 0);

        //Test world not equal
        Assert.assertNotSame("Location one & two should not equal each other", location, location2);

        location = new Location(world, 0, 1, 0);

        //Test pos not equal
        Assert.assertNotSame("Location one & two should not equal each other",location, location2);
    }

    @Test
    public void testList()
    {
        List<Location> list = new ArrayList();
        list.add(new Location(null, 0, 0, 0));

        //Test basic contains
        Assert.assertTrue("Should contain location", list.contains(new Location(null, 0, 0, 0)));
        Assert.assertTrue("Should not contain location", !list.contains(new Location(null, 0, 1, 0)));

        //Test removes
        list.remove(new Location(null, 0, 0, 0));
        Assert.assertTrue("Should not contain location", !list.contains(new Location(null, 0, 0, 0)));

        //Repeat with world not null
        World world = FakeWorld.newWorld("LocationTest");
        World world2 = FakeWorld.newWorld("LocationTest");

        list.add(new Location(world, 0, 0, 0));

        //Test basic contains
        Assert.assertTrue("Should contain location", list.contains(new Location(world, 0, 0, 0)));
        Assert.assertTrue("Should not contain location", !list.contains(new Location(world2, 0, 0, 0)));
    }

    public void testMap()
    {
        HashMap<Location, Object> map = new HashMap();
        map.put(new Location(null, 0, 0, 0), "yo");

        //Test basic contains
        Assert.assertTrue("Should contain location", map.containsKey(new Location(null, 0, 0, 0)));
        Assert.assertTrue("Should not contain location", !map.containsKey(new Location(null, 0, 1, 0)));

        //Test removes
        map.remove(new Location(null, 0, 0, 0));
        Assert.assertTrue("Should not contain location", !map.containsKey(new Location(null, 0, 0, 0)));

        //Repeat with world not null
        World world = FakeWorld.newWorld("LocationTest");
        World world2 = FakeWorld.newWorld("LocationTest");

        map.put(new Location(world, 0, 0, 0), "mama");

        //Test basic contains
        Assert.assertTrue("Should contain location", map.containsKey(new Location(world, 0, 0, 0)));
        Assert.assertTrue("Should not contain location", !map.containsKey(new Location(world2, 0, 0, 0)));
    }
}
