package com.builtbroken.mc.framework.block.imp;

import com.builtbroken.mc.api.IWorldPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended by listener classes that are applied to tiles or hosted by tiles.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public abstract interface ITileEventListener extends IWorldPosition
{
    /**
     * Get used to ID the listener group
     * this listener should be registered with
     *
     * @return unique key for the group
     */
    @Deprecated
    String getListenerKey();

    default boolean isValidForTile()
    {
        return true;
    }

    /**
     * Gets the ID(s) of the listener group
     * this listener should be registered with
     *
     * @return unique key for the group
     */
    default List<String> getListenerKeys()
    {
        List<String> list = new ArrayList();
        list.add(getListenerKey());
        return list;
    }
}
