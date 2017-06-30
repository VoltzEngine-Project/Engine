package com.builtbroken.test.lib.world.map.block;

import com.builtbroken.jlib.helpers.MathHelper;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataMap;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.nbt.NBTTagCompound;
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

    @Test
    public void testChunkAccess()
    {
        //Tests to ensure we can access several changes
        ExtendedBlockDataMap map = new ExtendedBlockDataMap(null, 1);
        for (int chunkX = 0; chunkX < 16; chunkX++)
        {
            for (int chunkZ = 0; chunkZ < 16; chunkZ++)
            {
                short r = MathHelper.randomShort();
                map.setValue(chunkX << 4, 1, chunkZ << 4, r);
                assertEquals(r, map.getValue(chunkX << 4, 1, chunkZ << 4));
            }
        }
    }

    @Test
    public void testLoad()
    {
        ExtendedBlockDataMap map = new ExtendedBlockDataMap(null, 1);
        NBTTagCompound tag = new NBTTagCompound();
        map.load(tag);

        assertNotNull(tag);
        assertTrue(tag.hasNoTags()); //TODO change when stuff is saved
    }

    @Test
    public void testSave()
    {
        ExtendedBlockDataMap map = new ExtendedBlockDataMap(null, 1);
        NBTTagCompound tag = new NBTTagCompound();
        map.save(tag);

        assertNotNull(tag);
        assertTrue(tag.hasNoTags()); //TODO change when stuff is saved
    }

    @Test
    public void testChunkSave()
    {
        ExtendedBlockDataMap map = new ExtendedBlockDataMap(null, 1);
        map.setValue(0, 0, 0, 1);
        map.setValue(100, 0, 0, 1);
        map.setValue(100, 0, 100, 1);
    }

    @Test
    public void testChunkLoad()
    {

    }
}
