package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.computer.DataSystemMethod;
import com.builtbroken.mc.api.computer.IDataSystem;
import com.builtbroken.mc.api.computer.IDataSystemMethod;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.ReflectionUtility;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Wrapper for annotation driven data system
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
@Deprecated
public class DataSystem implements IDataSystem
{
    public final String systemType;
    public final Class<? extends Object> clazz;

    private final HashMap<String, IDataSystemMethod> idToMethod = new HashMap();
    private final HashMap<String, IDataSystemMethod> invokeToMethod = new HashMap();

    private String[] methodNames;

    public DataSystem(String systemType, Class<? extends Object> clazz)
    {
        this.systemType = systemType;
        this.clazz = clazz;
        mapMethods();
    }

    private final void mapMethods()
    {
        try
        {
            List<Method> methods = ReflectionUtility.getAllMethods(clazz);
            for (Method method : methods)
            {
                DataSystemMethod dataSystemMethod = method.getAnnotation(DataSystemMethod.class);
                if (dataSystemMethod != null)
                {
                    method.setAccessible(true);
                    addMethod(new DataMethodReflection(dataSystemMethod.name(), dataSystemMethod.type(), method, convert(dataSystemMethod.args())));
                }
            }
        }
        catch (Exception e)
        {
            if (Engine.runningAsDev)
            {
                throw new RuntimeException("Failed to find invokeToMethod for DataSystem: " + this);
            }
            e.printStackTrace();
        }
    }

    private final DataArg[] convert(String[] strings)
    {
        if (strings != null && strings.length > 0 && !strings[0].equals(""))
        {
            DataArg[] args = new DataArg[strings.length];
            for (int i = 0; i < strings.length; i++)
            {
                String[] split = strings[i].split(":");
                args[i] = new DataArg(split[1], split[0]);
            }
            return args;
        }
        return null;
    }

    public IDataSystemMethod getMethod(String name)
    {
        return idToMethod.get(name.toLowerCase());
    }

    public void addMethod(IDataSystemMethod method)
    {
        String id = method.getInvokeName().toLowerCase();
        if (idToMethod.containsKey(id))
        {
            Engine.error("DataSystem#addMethod(" + method + ") registering method over existing method by id[" + id + "]");
        }
        idToMethod.put(id, method);
        invokeToMethod.put(method.getInvokeName(), method);
    }

    public String[] getMethodNames()
    {
        if (methodNames == null)
        {
            methodNames = invokeToMethod.keySet().stream().toArray(String[]::new);
        }
        return methodNames;
    }

    @Override
    public String getSystemType(Object host)
    {
        return systemType;
    }

    @Override
    public String[] getMethods(Object host)
    {
        return getMethodNames();
    }

    @Override
    public Object[] runMethod(Object host, String method_name, Object... args) throws Exception
    {
        IDataSystemMethod method = getMethod(method_name);
        if (method != null)
        {
            return method.callMethod(host, args);
        }
        throw new Exception("Error: Method '" + method_name + "' not found");
    }
}
