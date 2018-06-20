package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.computer.IDataSystem;

import java.util.HashMap;
import java.util.function.Function;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/20/2018.
 */
public class DataSystemLambda implements IDataSystem
{
    private final String id;

    //Stores exact name of the method
    private final HashMap<String, FunctionComputer> nameToMethod = new HashMap();

    //Stores lower case version of the method name
    private final HashMap<String, FunctionComputer> idToMethod = new HashMap();

    //Cache of method names for return only
    private String[] methodNames;

    /** Used for checking if a method should be applied to this system, do not edit */
    public final Object exampleInstance;

    public DataSystemLambda(String id, Object exampleInstance)
    {
        this.id = id;
        this.exampleInstance = exampleInstance;
    }

    public void addMethod(String name, Function<Object, Object> function)
    {
        addMethod(name, (host, method, args) -> runZeroArgMethod(name, host, args, function));
    }

    private Object runZeroArgMethod(String name, Object host, Object[] args, Function<Object, Object> function)
    {
        if (args == null || args.length == 0)
        {
            return function.apply(host);
        }
        return new Object[]{"Error: Method '" + name + "' + requires no arguments"};
    }

    public void addMethod(String name, FunctionComputer functionComputer)
    {
        methodNames = null;
        nameToMethod.put(name, functionComputer);
        idToMethod.put(name.toLowerCase(), functionComputer);
    }

    @Override
    public String getSystemType(Object host)
    {
        return id;
    }

    @Override
    public String[] getMethods(Object host)
    {
        if (methodNames == null)
        {
            methodNames = nameToMethod.keySet().stream().toArray(String[]::new);
        }
        return methodNames;
    }

    @Override
    public Object[] runMethod(Object host, String method, Object... args) throws Exception
    {
        FunctionComputer functionComputer = idToMethod.get(method.toLowerCase());
        if (functionComputer != null)
        {
            return getOut(functionComputer.apply(host, method, args));
        }
        return new Object[]{"Error: Method not found"};
    }

    protected Object[] getOut(Object out)
    {
        if (out == null)
        {
            return null;
        }
        else if (out.getClass().isArray())
        {
            return (Object[]) out;
        }
        return new Object[]{out};
    }
}
