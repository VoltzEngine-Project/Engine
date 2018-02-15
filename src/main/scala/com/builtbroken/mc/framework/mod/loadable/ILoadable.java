package com.builtbroken.mc.framework.mod.loadable;

import com.builtbroken.mc.framework.json.IJsonGenMod;

/**
 * Applied to loadable objects.
 *
 * @author Calclavia, Tgame14, Darkguardsman
 */
public interface ILoadable
{
    /**
     * Called before pre-init by your own mod implementing
     * {@link IJsonGenMod} from the method {@link IJsonGenMod#loadJsonContentHandlers()}
     * which is called from {@link com.builtbroken.mc.framework.json.JsonContentLoader} from
     * the {@link com.builtbroken.mc.core.Engine}'s loader.
     * <p>
     * Do:
     * Register Json processors
     * Register Json converters
     * Register Json related stuff
     */
    default void loadJsonContentHandlers() //TODO replace with registry event
    {
    }

    /**
     * Called on pre-init
     * Do:
     * Event registry
     * Item registry
     * Block registry
     * OreNames
     */
    default void preInit()
    {
    }

    /**
     * Called on init
     * Do:
     * Last min registry calls
     * Recipes
     * Mod compat
     */
    default void init()
    {
    }

    /**
     * Called on post init
     * Do:
     * Last min recipes
     * Last min mod compat
     */
    default void postInit()
    {
    }

    /**
     * Called at end of mod loading and each time a server starts
     * Do:
     * Cleanup
     */
    default void loadComplete()
    {
    }
}
