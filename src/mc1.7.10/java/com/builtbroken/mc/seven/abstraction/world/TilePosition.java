package com.builtbroken.mc.seven.abstraction.world;

import com.builtbroken.mc.abstraction.tile.ITilePosition;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class TilePosition implements ITilePosition
{
    private final WorldWrapper world;
    private final int x, y, z;

    public TilePosition(WorldWrapper world, int x, int y, int z) //1.12 turn to BlockPos
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public WorldWrapper getWorld()
    {
        return world;
    }

    @Override
    public int xCoord()
    {
        return x;
    }

    @Override
    public int yCoord()
    {
        return y;
    }

    @Override
    public int zCoord()
    {
        return z;
    }
}
