package com.builtbroken.mc.lib.json.processors;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.Loader;

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
    public boolean shouldLoad(JsonElement object)
    {
        if (object instanceof JsonObject)
        {
            if (((JsonObject) object).has("loadCondition"))
            {
                final String type = ((JsonObject) object).getAsJsonPrimitive("loadCondition").getAsString();
                if (type.startsWith("mod@"))
                {
                    String modName = type.substring(4, type.length());
                    if (!Loader.isModLoaded(modName))
                    {
                        return false;
                    }
                }
                else if (type.equalsIgnoreCase("devMode"))
                {
                    return Engine.runningAsDev;
                }
            }
        }
        return true;
    }
}
