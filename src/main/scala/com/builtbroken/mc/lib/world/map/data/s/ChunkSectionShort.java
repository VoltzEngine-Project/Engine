package com.builtbroken.mc.lib.world.map.data.s;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.ISave;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Used to stored data about a section of a chunk using a short value
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class ChunkSectionShort implements ISave
{
    public final short[] data = new short[4096]; //65,636 bits of memory usage

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

    public void setValue(int x, int y, int z, int value)
    {
        data[p(x, y, z)] = (short)value;
    }

    public void setValue(IPos3D pos, short value)
    {
        setValue(pos.xi(), pos.yi(), pos.zi(), value);
    }

    public void setValue(IPos3D pos, int value)
    {
        setValue(pos.xi(), pos.yi(), pos.zi(), value);
    }

    /**
     * Get the position in the data array for the 3D location
     * <p>
     * 3D -> 1D
     *
     * @param x - 0 - 15
     * @param y - 0 - 15
     * @param z - 0 - 15
     * @return position between 0 - 4095 inclusive
     */
    public int p(int x, int y, int z)
    {
        //http://sys.cs.rice.edu/course/comp314/10/p2/javabits.html
        //X take the first 4 bits
        //Y is the middle 4 bits
        //Z is the last 4 bits
        // XXXX YYYY ZZZZ
        //12bits, 1 1/2 bytes
        return x << 8 | y << 4 | z;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }
}
