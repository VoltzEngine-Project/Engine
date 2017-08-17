package com.builtbroken.mc.framework.block.imp;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IFillRainListener extends ITileEventListener
{
    /**
     * Called when rain hits the block
     */
    void onFilledWithRain();

    @Override
    default String getListenerKey()
    {
        return "rain";
    }
}
