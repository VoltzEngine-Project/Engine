package com.builtbroken.mc.framework.block.imp;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/10/2017.
 */
public interface ILightLevelListener extends ITileEventListener
{
    default int getLightLevel()
    {
        return -1;
    }

    @Override
    default String getListenerKey()
    {
        return "light";
    }
}
