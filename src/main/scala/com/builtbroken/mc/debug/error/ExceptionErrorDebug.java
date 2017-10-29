package com.builtbroken.mc.debug.error;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/28/2017.
 */
public class ExceptionErrorDebug implements IErrorDebug
{
    String title;
    String message;
    Exception exception;

    public ExceptionErrorDebug(String title, String message, Exception e)
    {
        this.title = title;
        this.message = message;
        this.exception = e;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getMessage()
    {
        return null;
    }
}
