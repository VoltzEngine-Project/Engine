package com.builtbroken.mc.core.asm.template;

/**
 * Applied to templates that may need additional calls in order to function
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public interface ITemplateCalls
{
    /** Called when tile loads into the world */
    default void load(Object tile)
    {
    }

    /** Called when tile unloads fromt the world */
    default void unload(Object tile)
    {
    }
}
