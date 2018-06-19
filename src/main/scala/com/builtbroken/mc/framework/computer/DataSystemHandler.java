package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.computer.IDataSystem;

import java.util.HashMap;

/**
 * Handles data systems
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public final class DataSystemHandler
{
    private static final HashMap<String, IDataSystem> typeToMethods = new HashMap();
    private static final HashMap<Class<? extends Object>, IDataSystem> clazzToSystem = new HashMap();

    public static IDataSystem getSystemInfo(Object host)
    {
        return clazzToSystem.get(host.getClass());
    }

    /**
     * Called to generate a data system from provide annotations
     *
     * @param uniqueName - unique id of the system
     * @param target     - target class for method invokes
     */
    public static void generate(String uniqueName, Class<? extends Object> target)
    {
        register(uniqueName, target, new DataSystemInfo(uniqueName, target));
    }

    /**
     * Called to register a data system
     *
     * @param uniqueName - unique id of the system
     * @param target     - target class to match the system against
     * @param system     - data system
     */
    public static void register(String uniqueName, Class<? extends Object> target, IDataSystem system)
    {
        clazzToSystem.put(target, system);
        typeToMethods.put(uniqueName, system);
    }
}
