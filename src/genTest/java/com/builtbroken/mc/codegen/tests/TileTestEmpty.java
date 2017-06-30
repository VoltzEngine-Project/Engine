package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.codegen.annotations.TileWrapped;

/**
 * Test tile to see if {@link com.builtbroken.mc.codegen.Main} functions correctly for tile processing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@TileWrapped(className = ".tile.TileEntityWrapperTestEmpty")
public final class TileTestEmpty extends TileNode
{
    public TileTestEmpty()
    {
        super("tile.test", "null");
    }
}
