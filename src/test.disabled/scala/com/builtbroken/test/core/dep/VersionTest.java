package com.builtbroken.test.core.dep;

import com.builtbroken.mc.core.deps.Version;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/12/2015.
 */
public class VersionTest extends TestCase
{
    @Test
    public void testInit()
    {
        Version version = new Version(1, 2, 3, 4);
        assertSame(1, version.major);
        assertSame(2, version.minor);
        assertSame(3, version.revis);
        assertSame(4, version.build);
    }

    @Test
    public void testInitString()
    {
        Version version = new Version("0.3.5b10");
        assertSame(0, version.major);
        assertSame(3, version.minor);
        assertSame(5, version.revis);
        assertSame(10, version.build);
    }

    @Test
    public void testIsNewer()
    {
        Version version = new Version("0.3.5b10");
        Version version2 = new Version("0.3.5b10");
        assertFalse(version.isNewer(version2));
        assertFalse(version2.isNewer(version));

        version2 = new Version("0.3.5b11");
        assertTrue(version.isNewer(version2));
        assertFalse(version2.isNewer(version));

        version2 = new Version("0.3.6b11");
        assertTrue(version.isNewer(version2));
        assertFalse(version2.isNewer(version));

        version2 = new Version("0.4.5b11");
        assertTrue(version.isNewer(version2));
        assertFalse(version2.isNewer(version));

        version2 = new Version("1.3.5b11");
        assertTrue(version.isNewer(version2));
        assertFalse(version2.isNewer(version));
    }
}
