package com.builtbroken.test.lib.world.map.data;

import com.builtbroken.jlib.helpers.MathHelper;
import com.builtbroken.mc.lib.world.map.data.s.ChunkDataShort;
import com.builtbroken.mc.lib.world.map.data.s.ChunkSectionShort;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2017.
 */
public class TestChunkDataShort extends TestCase
{
    @Test
    public void testInit()
    {
        ChunkDataShort chunk = new ChunkDataShort(10, 13);
        assertEquals(10, chunk.position.chunkXPos);
        assertEquals(13, chunk.position.chunkZPos);
    }

    @Test
    public void testSetValue()
    {
        ChunkDataShort chunk = new ChunkDataShort(10, 13);

        assertEquals(0, chunk.setValue(10, 11, 15, 100));
        assertNotNull(chunk.sections[0]);
        assertEquals(100, chunk.sections[0].getValue(10, 11, 15));

        assertEquals(100, chunk.setValue(10, 11, 15, 321));
        assertNotNull(chunk.sections[0]);
        assertEquals(321, chunk.sections[0].getValue(10, 11, 15));

        assertEquals(0, chunk.setValue(10, 20, 15, 321));
        assertNotNull(chunk.sections[1]);
        assertEquals(321, chunk.sections[1].getValue(10, 4, 15));
    }

    @Test
    public void testGetValue()
    {
        ChunkDataShort chunk = new ChunkDataShort(10, 13);
        chunk.sections[1] = new ChunkSectionShort();
        chunk.sections[1].data[1] = 67;
        assertEquals(67, chunk.getValue(0, 16, 1));
    }

    @Test
    public void testY()
    {
        //Test that all y levels work
        ChunkDataShort chunk = new ChunkDataShort(10, 13);
        for (int i = 0; i < 256; i++)
        {
            chunk.setValue(0, i, 0, i);
            assertEquals(i, chunk.getValue(0, i, 0));
        }
    }

    @Test
    public void testAllSlots()
    {
        //Test that every slot works
        ChunkDataShort chunk = new ChunkDataShort(10, 13);
        for (int x = 0; x < 16; x++)
        {
            for (int y = 0; y < 256; y++)
            {
                for (int z = 0; z < 16; z++)
                {
                    int r = MathHelper.randomShort();
                    chunk.setValue(x, y, z, r);
                    assertEquals(r, chunk.getValue(x, y, z));
                }
            }
        }
    }
}
