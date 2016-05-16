package com.builtbroken.mc.framework.tile.api;

import net.minecraft.world.IBlockAccess;

/**
 * Applied to {@link ITileHost} that are exclusively for handling {@link net.minecraft.block.Block}
 * method calls.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2016.
 */
public interface IBlockTile extends ITileHost
{
    /**
     * Called for handling static reference data, where
     * the instance of the tile will need to be
     * injected with its location before each
     * method call
     *
     * @param world - in most cases this is the world object so check for
     *              instance of world as needed.
     * @param x
     * @param y
     * @param z
     */
    void injectLocation(IBlockAccess world, int x, int y, int z);
}
