package com.builtbroken.mc.testing.tile;

import com.builtbroken.mc.prefab.tile.BlockTile;
import com.builtbroken.mc.prefab.tile.Tile;

/** Prefab for testing Tile objects. Tries to
 * Created by Dark on 9/11/2015.
 */
public class AbstractTileTest<T extends Tile, B extends BlockTile> extends AbstractTileEntityTest
{
    public AbstractTileTest(B block, String name, Class<T> tileClazz)
    {
        super(block, name, tileClazz);
    }
}
