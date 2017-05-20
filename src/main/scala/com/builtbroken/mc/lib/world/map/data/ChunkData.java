package com.builtbroken.mc.lib.world.map.data;

import net.minecraft.world.ChunkCoordIntPair;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public abstract class ChunkData
{
    public final ChunkCoordIntPair position;

    public ChunkData(int x, int z)
    {
        position = new ChunkCoordIntPair(x, z);
    }
}
