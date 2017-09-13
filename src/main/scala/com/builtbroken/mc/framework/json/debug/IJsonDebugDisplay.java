package com.builtbroken.mc.framework.json.debug;

import com.builtbroken.mc.framework.json.imp.IJsonGenObject;

import java.util.List;

/**
 * Applied to objects that can control the display of debug information
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public interface IJsonDebugDisplay extends IJsonGenObject
{
    default String getDisplayName()
    {
        return getMod() + ":" + getContentID();
    }

    default void addDebugLines(List<String> lines)
    {

    }

    default void onDoubleClickLine()
    {

    }
}
