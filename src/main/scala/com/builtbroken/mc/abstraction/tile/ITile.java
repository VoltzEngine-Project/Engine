package com.builtbroken.mc.abstraction.tile;

import com.builtbroken.mc.abstraction.data.ITileData;

/**
 * Wrapper object for a block at a location in the world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public interface ITile extends ITilePosition
{
    /**
     * Gets the tiles material
     *
     * @return
     */
    ITileMaterial getMaterial();

    /**
     * Gets the data defining the tile
     *
     * @return
     */
    ITileData getData();
}
