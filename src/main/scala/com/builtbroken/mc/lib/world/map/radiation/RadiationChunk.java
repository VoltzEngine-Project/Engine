package com.builtbroken.mc.lib.world.map.radiation;

import com.builtbroken.mc.lib.world.map.data.ChunkData;
import com.builtbroken.mc.lib.world.map.data.ChunkSectionShort;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class RadiationChunk extends ChunkData
{
    protected final ChunkSectionShort[] sections = new ChunkSectionShort[16];

    public RadiationChunk(int x, int z)
    {
        super(x, z);
    }

    public short getValue(int x, int y, int z)
    {
        return 0;
    }

    public void setValue(int x, int y, int z, short value)
    {

    }
}
