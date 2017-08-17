package com.builtbroken.mc.framework.block.imp;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * Used to build a listener object for json based system
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2017.
 */
public interface ITileEventListenerBuilder
{
    default ITileEventListener createListener(Block block)
    {
        return null;
    }

    default ITileEventListener createListener(TileEntity tileEntity)
    {
        return null;
    }

    /**
     * Get of the listener this
     * builds
     *
     * @return key
     */
    String getListenerKey();
}
