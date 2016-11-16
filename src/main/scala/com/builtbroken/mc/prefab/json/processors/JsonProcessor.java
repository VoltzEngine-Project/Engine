package com.builtbroken.mc.prefab.json.processors;

import com.builtbroken.mc.prefab.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public abstract class JsonProcessor<D extends IJsonGenObject>
{
    /**
     * The mod that owns this processor
     *
     * @return mod as domain name
     */
    public abstract String getMod();

    /**
     * String used to sort entries in the map
     *
     * @return
     */
    public abstract String getSortingString();

    /**
     * List of string keys that are the main headers for the json files
     * that this processor can handle.
     *
     * @return
     */
    public abstract List<String> getJsonKeyThatCanBeProcessed();

    /**
     * Called to process a json block section
     *
     * @return
     */
    public boolean canProcess(JsonElement element)
    {
        return true;
    }

    public D process(JsonElement element)
    {
        return null;
    }
}
