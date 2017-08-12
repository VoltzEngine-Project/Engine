package com.builtbroken.mc.framework.block.imp;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface ITileWithListeners
{
    /**
     * Called to get the listeners for a tile
     *
     * @param key - unique group ID
     * @return list of listeners or empty list
     */
    List<ITileEventListener> getListeners(String key);
}
