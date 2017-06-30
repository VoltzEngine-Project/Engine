package com.builtbroken.test.lib.json;

import com.builtbroken.mc.lib.json.loading.ProcessorKeySorter;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/17/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestJsonStringSorter extends AbstractTest
{
    public void testSorting()
    {
        List<String> list = new ArrayList();
        list.add("ammo@after:ammoType");
        list.add("block");
        list.add("ammoType@after:item");
        list.add("item");
        list.add("tile");
        list.add("clip@after:ammoType");
        list.add("gun@after:ammoType");
        Collections.sort(list, new ProcessorKeySorter());

        assertSame("block", list.get(0));
        assertSame("item", list.get(1));
        assertSame("tile", list.get(2));
        //Anything after those 3 doesn't matter on order we only need none tagged near top for faster sorting later
    }
}
