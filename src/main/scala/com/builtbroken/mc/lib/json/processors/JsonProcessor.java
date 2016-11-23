package com.builtbroken.mc.lib.json.processors;

import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
     * Gets the key that this processor uses
     * for most of it's actions.
     * <p>
     * This is mainly used to help sort
     * entries before processing so items
     * load in order.
     *
     * @return key
     */
    public abstract String getJsonKey();

    /**
     * Gets the order of loading
     * <p>
     * use values (after:key) or (before:key)
     *
     * @return string
     */
    public abstract String getLoadOrder();

    /**
     * Called to process a json block section
     *
     * @return
     */
    public boolean canProcess(String key, JsonElement element)
    {
        return key.equalsIgnoreCase(getJsonKey());
    }

    public abstract D process(JsonElement element);

    /**
     * Quick way to check that required fields exist in the json file
     *
     * @param object
     * @param values
     */
    public void ensureValuesExist(JsonObject object, String... values)
    {
        for (String value : values)
        {
            if (!object.has(value))
            {
                throw new IllegalArgumentException("File is missing " + value + " value " + object);
            }
        }
    }
}
