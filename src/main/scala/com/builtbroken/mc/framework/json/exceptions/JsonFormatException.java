package com.builtbroken.mc.framework.json.exceptions;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/9/2017.
 */
public class JsonFormatException extends Exception
{
    public JsonFormatException(String msg)
    {
        super(msg);
    }

    public JsonFormatException(String msg, Throwable e)
    {
        super(msg, e);
    }
}
