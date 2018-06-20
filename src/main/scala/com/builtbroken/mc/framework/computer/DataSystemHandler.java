package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.computer.IDataSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Handles data systems
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public final class DataSystemHandler
{
    public static int parentSearchLimit = 10;

    private static final HashMap<String, Function<Object, FunctionComputer>> sharedMethods = new HashMap();

    private static final HashMap<String, IDataSystem> typeToSystem = new HashMap();
    private static final HashMap<Class<? extends Object>, IDataSystem> clazzToSystem = new HashMap();
    private static final HashMap<Class<? extends Object>, Function<Object, Object>> clazzToRedirect = new HashMap();

    private static final List<DataSystemLambda> lambdaDataSystems = new ArrayList();

    public static IDataSystem getSystemInfo(Object host)
    {
        IDataSystem system = getSystem(host);
        if (system == null)
        {
            return getSystemForRedirect(host);
        }
        return null;
    }

    private static IDataSystem getSystemForRedirect(Object host)
    {
        Class clazz = host.getClass();
        //Try to see if its a redirect tile
        if (clazzToRedirect.containsKey(clazz))
        {
            return getSystemInfo(clazzToRedirect.get(clazz).apply(host));
        }
        else
        {
            //If failed, try super class(s)... only if we didn't already try once before
            if (!clazzToRedirect.containsKey(clazz))
            {
                int depth = 0;
                do
                {
                    clazz = clazz.getSuperclass();
                }
                while (!clazzToRedirect.containsKey(clazz)
                        && clazz.getSuperclass() != null
                        && clazz.getSuperclass() != Object.class
                        && depth++ < parentSearchLimit);

                if (clazzToRedirect.containsKey(clazz))
                {
                    clazzToRedirect.put(host.getClass(), clazzToRedirect.get(clazz));
                }
                else
                {
                    clazzToRedirect.put(host.getClass(), null);
                }
            }
        }
        return null;
    }

    private static IDataSystem getSystem(Object host)
    {
        Class clazz = host.getClass();
        //Try to get by class
        IDataSystem system = clazzToSystem.get(clazz);

        //If failed, try super class(s)... only if we didn't already try once before
        if (system == null && !clazzToSystem.containsKey(clazz))
        {
            int depth = 0;
            do
            {
                clazz = clazz.getSuperclass();
                system = clazzToSystem.get(clazz);
            }
            while (system == null
                    && clazz.getSuperclass() != null
                    && clazz.getSuperclass() != Object.class
                    && depth++ < parentSearchLimit);

            if (system != null)
            {
                clazzToSystem.put(host.getClass(), system);
            }
            else
            {
                clazzToSystem.put(host.getClass(), null);
            }
        }
        return system;
    }

    /**
     * Called to generate a data system from provide annotations
     *
     * @param uniqueName - unique id of the system
     * @param target     - target class for method invokes
     */
    @Deprecated //Annotation driven system is no longer used
    public static void generate(String uniqueName, Class<? extends Object> target)
    {
        register(uniqueName, target, new DataSystemReflection(uniqueName, target));
    }

    /**
     * Creates a redirect from one tile to another tile
     *
     * @param instance    - used to get the class and other useful data
     * @param getFunction - function to call to get the real tile
     */
    public static void createRedirectTile(Object instance, Function<Object, Object> getFunction)
    {
        clazzToRedirect.put(instance.getClass(), getFunction);
    }

    /**
     * Called to generate a new lambda based data system
     * <p>
     * Once generated shared methods will be mapped automaticly.
     *
     * @param uniqueName - unique id of the system
     * @param instance   - instance of the tile, used to get methods and class
     * @return data system
     */
    public static DataSystemLambda createNewDataSystem(String uniqueName, Object instance)
    {
        DataSystemLambda dataSystemLambda = new DataSystemLambda(uniqueName, instance);
        register(uniqueName, instance.getClass(), dataSystemLambda);
        lambdaDataSystems.add(dataSystemLambda);
        collectSharedMethods(instance, dataSystemLambda);
        return dataSystemLambda;
    }

    /**
     * Called to add a method that should be shared over several types.
     * <p>
     * idea use for this is to map very common methods
     * Example:
     * getEnergy()
     * getEnergyCapacity()
     * <p>
     * When method is added late it will be applied to any existing data systems.
     *
     * @param name     - name of the method, try to make it unqiue as there is no overlap handling
     * @param function - function to get the method to call
     */
    public static void addSharedMethod(String name, Function<Object, FunctionComputer> function)
    {
        sharedMethods.put(name, function);

        for (DataSystemLambda system : lambdaDataSystems)
        {
            FunctionComputer method = function.apply(system.exampleInstance);
            if (method != null)
            {
                system.addMethod(name, method);
            }
        }
    }

    private static void collectSharedMethods(Object instance, DataSystemLambda dataSystemLambda)
    {
        for (Map.Entry<String, Function<Object, FunctionComputer>> entry : sharedMethods.entrySet())
        {
            String name = entry.getKey();
            Function<Object, FunctionComputer> provider = entry.getValue();
            FunctionComputer method = provider.apply(instance);
            if (method != null)
            {
                dataSystemLambda.addMethod(name, method);
            }
        }
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
        typeToSystem.put(uniqueName, system);
    }
}
