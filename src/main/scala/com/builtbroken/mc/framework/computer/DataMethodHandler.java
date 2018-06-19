package com.builtbroken.mc.framework.computer;

import java.util.HashMap;

/**
 * Collects methods that can be called on a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public final class DataMethodHandler
{
    private static final HashMap<String, DataSystemInfo> typeToMethods = new HashMap();
    private static final HashMap<Class<? extends Object>, DataSystemInfo> clazzToSystem = new HashMap();

    public static DataSystemInfo getSystemInfo(Object host)
    {
        return clazzToSystem.get(host.getClass());
    }
}
