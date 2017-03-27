package com.builtbroken.mc.lib.json.imp;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.json.IJsonGenMod;

/**
 * Applied to all objects created from Json data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public interface IJsonGenObject
{
    /**
     * Called at some point after {@link #register()}
     * to register the content to the game itself.
     * <p>
     * This is called several times so make sure
     * to check if the content has already been
     * registered. This can easily be handled by
     * using a boolean check in the object.
     *
     * @param mod     - mod registering the content
     * @param manager - manager used to register the content
     */
    default void register(IJsonGenMod mod, ModManager manager)
    {

    }


    /**
     * Called when the gen object has
     * been created and registered to
     * {@link com.builtbroken.mc.lib.json.JsonContentLoader}
     */
    default void register()
    {
    }

    default void setAuthor(String name)
    {

    }

    /**
     * Unique key of the loader that created
     * this gen object.
     *
     * @return key for loader
     */
    String getLoader();

    /**
     * Gets the mod this content belongs to
     *
     * @return mod domain ID
     */
    String getMod();
}
