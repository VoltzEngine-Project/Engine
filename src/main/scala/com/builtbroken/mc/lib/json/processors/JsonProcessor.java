package com.builtbroken.mc.lib.json.processors;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.Loader;

import java.util.List;

/**
 * Default implementation for processor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public abstract class JsonProcessor<D extends IJsonGenObject> implements IJsonProcessor
{
    @Override
    public boolean canProcess(String key, JsonElement element)
    {
        return key.equalsIgnoreCase(getJsonKey());
    }

    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> entries)
    {
        D output = process(element);
        if (output != null)
        {
            entries.add(output);
        }
        return true;
    }

    public D process(JsonElement element)
    {
        return null;
    }

    /**
     * Quick way to check that required fields exist in the json file
     *
     * @param object
     * @param values
     */
    public static void ensureValuesExist(JsonObject object, String... values)
    {
        for (String value : values)
        {
            if (!object.has(value))
            {
                throw new IllegalArgumentException("File is missing " + value + " value " + object);
            }
        }
    }

    @Override
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
