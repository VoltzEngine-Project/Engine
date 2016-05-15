package com.builtbroken.mc.framework.tile.api;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.prefab.tile.interfaces.tile.ITile;

/**
 * Applied to the object that hosts a tile, normally this is a TileEntity wrapper object. However,
 * this can be just about anything so long as the data is provided.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2016.
 */
public interface ITileHost extends IWorldPosition
{
    /**
     * Gets the current tile for this block
     *
     * @return tile at the location
     */
    ITile getTile();

    /**
     * Is this host a block
     *
     * @return true if the block is a block
     */
    boolean isBlock();
}
