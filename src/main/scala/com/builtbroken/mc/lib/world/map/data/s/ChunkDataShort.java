package com.builtbroken.mc.lib.world.map.data.s;

import com.builtbroken.mc.lib.world.map.data.ChunkData;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class ChunkDataShort extends ChunkData
{
    public final ChunkSectionShort[] sections = new ChunkSectionShort[16];

    public ChunkDataShort(int x, int z)
    {
        super(x, z);
    }

    public short getValue(int x, int y, int z)
    {
        int s = y / 16;
        if (sections[s] == null)
        {
            sections[s] = new ChunkSectionShort();
        }
        return sections[s].getValue(x, y & 15, z);
    }

    public short setValue(int x, int y, int z, int value)
    {
        return setValue(x, y, z, (short) value);
    }

    public short setValue(int x, int y, int z, short value)
    {
        int s = y / 16;
        if (sections[s] == null)
        {
            sections[s] = new ChunkSectionShort();
        }
        short prev = sections[s].getValue(x, y & 15, z);
        sections[s].setValue(x, y & 15, z, value);
        return prev;
    }
}
