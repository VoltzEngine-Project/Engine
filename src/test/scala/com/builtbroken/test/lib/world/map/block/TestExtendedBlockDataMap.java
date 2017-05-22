package com.builtbroken.test.lib.world.map.block;

import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataMap;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2017.
 */
@RunWith(VoltzTestRunner.class)
public class TestExtendedBlockDataMap extends AbstractTest
{
    @Test
    public void testInit()
    {
        ExtendedBlockDataMap map = new ExtendedBlockDataMap(null, 1);
        assertEquals(1, map.dimID());
    }

    @Test
    public void testSetGetValue()
    {
        ExtendedBlockDataMap map = new ExtendedBlockDataMap(null, 1);
        assertEquals(0, map.setValue(10, 40, 12, 897));
        assertEquals(897, map.getValue(10, 40, 12));

        assertEquals(897, map.setValue(10, 40, 12, 234));
        assertEquals(234, map.getValue(10, 40, 12));
    }
}
