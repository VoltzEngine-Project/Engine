package com.builtbroken.mc.seven.framework.block.listeners.client;

import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/17/2017.
 */
public interface ITileRenderListener extends ITileEventListener
{
    default void renderDynamic(TileEntity tile, double xx, double yy, double zz, float f)
    {

    }

    default String getListenerKey()
    {
        return "tilerender";
    }
}
