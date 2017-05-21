package com.builtbroken.mc.lib.world.map.data.s;

import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * Used to stored data about a section of a chunk using a short value
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class ChunkSectionShort
{
    protected final short[] data = new short[4096]; //65,636 bits of memory usage

    public short getValue(int x, int y, int z)
    {
        int p = p(x, y, z);
        if (p >= 0 && p < data.length)
        {
            return data[p];
        }
        return 0;
    }

    public short getValue(IPos3D pos)
    {
        return getValue(pos.xi(), pos.yi(), pos.zi());
    }

    public void setValue(int x, int y, int z, short value)
    {
        data[p(x, y, z)] = value;
    }

    public void setValue(IPos3D pos, short value)
    {
        setValue(pos.xi(), pos.yi(), pos.zi(), value);
    }

    protected int p(int x, int y, int z)
    {
        return x << 8 | y << 4 | z;
    }
}
