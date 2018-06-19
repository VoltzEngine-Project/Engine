package com.builtbroken.mc.framework.computer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public class DataArg
{
    public final String name;
    public final String type;

    public DataArg(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    public Object convert(Object input)
    {
        //TODO pass through converter system to allow adding more supported types
        if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer"))
        {
            if (input instanceof Number)
            {
                return ((Number) input).intValue();
            }
            else if (input instanceof String)
            {
                return Integer.parseInt(((String) input).trim());
            }
        }
        else if (type.equalsIgnoreCase("float"))
        {
            if (input instanceof Number)
            {
                return ((Number) input).floatValue();
            }
            else if (input instanceof String)
            {
                return Float.parseFloat(((String) input).trim());
            }
        }
        else if (type.equalsIgnoreCase("double"))
        {
            if (input instanceof Number)
            {
                return ((Number) input).doubleValue();
            }
            else if (input instanceof String)
            {
                return Double.parseDouble(((String) input).trim());
            }
        }
        else if (type.equalsIgnoreCase("string"))
        {
            return input.toString().trim();
        }
        return null;
    }
}
