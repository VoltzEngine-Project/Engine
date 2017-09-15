package com.builtbroken.mc.core.registry.implement;

/**
 * Called when the game has finished loading all mods
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
@Deprecated
public interface ILoadComplete
{
    /**
     * Called when the game finished loadiing,
     * use this to override data that was
     * processed in postInit();
     * <p>
     * Do not use lightly as this really should
     * not be used much.
     */
    void onLoadCompleted();
}
