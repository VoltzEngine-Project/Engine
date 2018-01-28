package com.builtbroken.test.lib.world.map.data;

import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.world.map.data.s.ChunkSectionShort;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2017.
 */
public class TestChunkSectionShort extends TestCase
{
    @Test
    public void testPoint()
    {
        ChunkSectionShort chunk = new ChunkSectionShort();
        assertEquals(0, chunk.p(0, 0, 0));

        //Test Z
        for (int i = 0; i < 16; i++)
        {
            assertEquals(i, chunk.p(0, 0, i));
        }

        //Test Y
        for (int i = 0; i < 16; i++)
        {
            assertEquals(i * 16, chunk.p(0, i, 0));
        }

        //Test X
        for (int i = 0; i < 16; i++)
        {
            assertEquals(i * 256, chunk.p(i, 0, 0));
        }

        for (int x = 0; x < 16; x++)
        {
            for (int y = 0; y < 16; y++)
            {
                for (int z = 0; z < 16; z++)
                {
                    assertEquals(x * 256 + y * 16 + z, chunk.p(x, y, z));
                }
            }
        }
    }

    @Test
    public void testGetValue()
    {
        ChunkSectionShort chunk = new ChunkSectionShort();
        chunk.data[10] = 15;

        assertEquals(15, chunk.getValue(0, 0, 10));

        chunk.data[256] = 357;
        assertEquals(357, chunk.getValue(1, 0, 0));
    }

    @Test
    public void testGetValuePos()
    {
        ChunkSectionShort chunk = new ChunkSectionShort();
        chunk.data[10] = 15;

        assertEquals(15, chunk.getValue(new Pos(0, 0, 10.13)));

        chunk.data[256] = 357;
        assertEquals(357, chunk.getValue(new Pos(1, 0, 0)));
    }

    @Test
    public void testSetValue()
    {
        ChunkSectionShort chunk = new ChunkSectionShort();
        chunk.data[10] = 15;

        chunk.setValue(0, 0, 10, 20);
        assertEquals(20, chunk.data[10]);

        chunk.data[256] = 357;
        chunk.setValue(1, 0, 0, 432);
        assertEquals(432, chunk.data[256]);
    }
}
