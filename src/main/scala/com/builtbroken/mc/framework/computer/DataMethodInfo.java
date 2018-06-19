package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.computer.DataMethodType;
import com.builtbroken.mc.api.computer.IDataSystemMethod;
import com.builtbroken.mc.lib.helper.LanguageUtility;

import java.lang.reflect.Method;

/**
 * Store information about a single method
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public class DataMethodInfo implements IDataSystemMethod
{
    /** Display name of the method */
    public final String name;
    public final DataMethodType type;
    public final Method method;
    public final DataArg[] args;

    //Name given to computer systems to call
    private final String invokeName;

    public DataMethodInfo(String name, DataMethodType type, Method method, DataArg[] args)
    {
        this.name = name;
        this.type = type;
        this.method = method;
        this.args = args;

        switch (type)
        {
            case GET:
                invokeName = "get" + LanguageUtility.capitalizeFirst(name);
                break;

            case SET:
                invokeName = "set" + LanguageUtility.capitalizeFirst(name);
                break;

            case INVOKE:
            default:
                invokeName = LanguageUtility.decapitalizeFirst(name);
                break;
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getInvokeName()
    {
        return invokeName;
    }

    @Override
    public DataMethodType getMethodType()
    {
        return type;
    }

    @Override
    public Object[] callMethod(Object system, Object... inputData) throws Exception
    {
        if (method != null)
        {
            if (args == null)
            {
                if (inputData == null || inputData.length == 0)
                {
                    return getOut(method.invoke(system));
                }
                else
                {
                    throw new Exception("Error: Method has no input arguments");
                }
            }
            else if (inputData.length == args.length)
            {
                try
                {
                    Object[] data = new Object[args.length];
                    int i = 0;
                    for (DataArg arg : args)
                    {
                        Object input = inputData[i];
                        Object c = arg.convert(input);
                        if (c != null)
                        {
                            data[i++] = c;
                        }
                        else
                        {
                            throw new Exception("Error: failed to convert argument " + i + " of value " + input);
                        }
                    }

                    return getOut(method.invoke(system, data));
                }
                catch (Exception e)
                {
                    throw new Exception("Error: " + e.getMessage());
                }
            }
            else
            {
                throw new Exception("Error: Expected " + args.length + " arguments but received " + inputData.length);
            }
        }
        throw new Exception("Error: No method provided");
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
