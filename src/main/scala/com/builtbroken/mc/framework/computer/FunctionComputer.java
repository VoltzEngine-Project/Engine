package com.builtbroken.mc.framework.computer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/20/2018.
 */
@FunctionalInterface
public interface FunctionComputer
{
    Object apply(Object host, String method, Object[] args);
}
