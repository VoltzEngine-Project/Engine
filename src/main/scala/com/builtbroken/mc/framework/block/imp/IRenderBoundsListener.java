package com.builtbroken.mc.framework.block.imp;

import com.builtbroken.mc.imp.transform.region.Cube;

/**
 * Used to modify the render bounds of the tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/14/2017.
 */
public interface IRenderBoundsListener extends ITileEventListener
{
    default Cube getRenderBounds()
    {
        return null;
    }
}
