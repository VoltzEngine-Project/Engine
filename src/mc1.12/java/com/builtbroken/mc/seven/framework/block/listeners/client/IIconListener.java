package com.builtbroken.mc.seven.framework.block.listeners.client;

import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2017.
 */
public interface IIconListener extends ITileEventListener
{
    default ResourceLocation getTileIcon(int side, IBlockState state)
    {
        return null;
    }

    @Override
    default String getListenerKey()
    {
        return "icon";
    }
}
