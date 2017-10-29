package com.builtbroken.mc.debug.error;

/**
 * Applied to object that contains error information for display in a debug system
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/28/2017.
 */
public interface IErrorDebug
{
    String getTitle();

    String getMessage();
}
