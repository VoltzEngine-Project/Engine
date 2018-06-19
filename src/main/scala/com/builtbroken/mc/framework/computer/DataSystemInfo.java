package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.computer.DataSystemMethod;
import com.builtbroken.mc.api.computer.IDataSystem;
import com.builtbroken.mc.api.computer.IDataSystemMethod;
import com.builtbroken.mc.lib.helper.ReflectionUtility;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public class DataSystemInfo implements IDataSystem
{
    public final String systemType;
    public final Class<? extends IDataSystem> clazz;

    private HashMap<String, IDataSystemMethod> methods;

    private String[] methodNames;

    public DataSystemInfo(String systemType, Class<? extends IDataSystem> clazz)
    {
        this.systemType = systemType;
        this.clazz = clazz;
        try
        {
            List<Method> methods = ReflectionUtility.getAllMethods(clazz);
            for (Method method : methods)
            {
                DataSystemMethod dataSystemMethod = method.getAnnotation(DataSystemMethod.class);
                if (dataSystemMethod != null)
                {
                    addMethod(new DataMethodInfo(dataSystemMethod.name(), dataSystemMethod.type(), method, convert(dataSystemMethod.args())));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private final DataArg[] convert(String[] strings)
    {
        DataArg[] args = new DataArg[strings.length];
        for (int i = 0; i < strings.length; i++)
        {
            String[] split = strings[i].split(":");
            args[i] = new DataArg(split[1], split[0]);
        }
        return args;
    }

    public IDataSystemMethod getMethod(String name)
    {
        return methods.get(name.toLowerCase());
    }

    public void addMethod(IDataSystemMethod method)
    {
        int i = 0;
        String id = method.getInvokeName().toLowerCase();
        do
        {
            if (methods.containsKey(id))
            {
                id = method.getInvokeName().toLowerCase() + (i++);
            }
            else
            {
                methods.put(id, method);
                break;
            }
        }
        while (i < 100);
    }

    public String[] getMethodNames()
    {
        if (methodNames == null)
        {
            methodNames = new String[methods.size()];
            int i = 0;
            for (IDataSystemMethod method : methods.values())
            {
                methodNames[i++] = method.getName();
            }
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
