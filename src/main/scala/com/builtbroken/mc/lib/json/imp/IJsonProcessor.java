package com.builtbroken.mc.lib.json.imp;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * Applied to objects that handle loading json files and converting them into data objects
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public interface IJsonProcessor
{
    /**
     * The mod that owns this processor
     *
     * @return mod as domain name
     */
    String getMod();

    /**
     * Gets the key that this processor uses
     * for most of it's actions.
     * <p>
     * This is mainly used to help sort
     * entries before processing so items
     * load in order.
     *
     * @return key
     */
    String getJsonKey();

    /**
     * Gets the order of loading
     * <p>
     * use values (after:key) or (before:key)
     *
     * @return string
     */
    String getLoadOrder();

    /**
     * Called to process a json block section
     *
     * @return
     */
    boolean canProcess(String key, JsonElement element);

    /**
     * Called to process the element data
     *
     * @param element - data
     * @param entries - list to add generated object to
     * @return true if everything was handled
     */
    boolean process(JsonElement element, List<IJsonGenObject> entries);

    /**
     * Called to check if the processor should load the
     * data and convert it.
     * <p>
     * Implemented separately from {@link #canProcess(String, JsonElement)}
     * so to note that the processor could have loaded. However, some
     * conditional has said for the content to not load.
     *
     * @param object - object entry of the data
     * @return true if it should load
     */
    default boolean shouldLoad(JsonElement object)
    {
        return true;
    }
}
