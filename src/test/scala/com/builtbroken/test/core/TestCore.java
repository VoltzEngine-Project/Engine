package com.builtbroken.test.core;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/29/2017.
 */
@RunWith(VoltzTestRunner.class)
public class TestCore extends AbstractTest
{
    @Test
    public void testJUnitCheck()
    {
        assertTrue(Engine.isJUnitTest());
    }
}
