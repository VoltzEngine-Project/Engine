package com.builtbroken.mc.lib.json.conversion;

import com.google.gson.JsonElement;

/**
 * Handles taking data from json and converting it into something that can be injected.
 * <p>
 * Primitive types are already handled by most processors. This should only be
 * used to convert json to usable objects.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2017.
 */
public abstract class JsonConverter<I extends Object>
{
    /**
     * Key used to ID this converter when matching data type.
     * this is not the key in the json data, think int, float, double
     */
    public final String key;

    public JsonConverter(String key)
    {
        this.key = key;
    }

    /**
     * Called to convert the json element into
     * an object.
     *
     * @param element - element that was marked with the type
     * @return object, or null if can't convert
     */
    public abstract I convert(JsonElement element);
}
