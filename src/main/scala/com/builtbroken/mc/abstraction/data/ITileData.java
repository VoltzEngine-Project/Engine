package com.builtbroken.mc.abstraction.data;

/**
 * Wrapper for block data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/15/2017.
 */
public interface ITileData
{

    /**
     * Internal call to unwrap the tile data
     * back to a Block instance
     *
     * @return block
     */
    Object unwrap();
}
