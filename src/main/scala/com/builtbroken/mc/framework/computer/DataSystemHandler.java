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
    private static final HashMap<String, Function<Object, FunctionComputer>> sharedMethods = new HashMap();

    private static final HashMap<String, IDataSystem> typeToSystem = new HashMap();
    private static final HashMap<Class<? extends Object>, IDataSystem> clazzToSystem = new HashMap();

    private static final List<DataSystemLambda> lambdaDataSystems = new ArrayList();

    public static IDataSystem getSystemInfo(Object host)
    {
        IDataSystem system = clazzToSystem.get(host.getClass());
        if (system == null)
        {
            system = clazzToSystem.get(host.getClass().getSuperclass());
            //TODO move through super classes looking for registered node
            //TODO cache failed classes so not to re-loop
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
