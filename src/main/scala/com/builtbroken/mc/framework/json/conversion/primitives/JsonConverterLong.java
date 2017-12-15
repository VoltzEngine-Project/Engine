package com.builtbroken.mc.framework.json.conversion.primitives;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Wrapper for converting JSON element to string
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonConverterLong extends JsonConverter<Long>
{
    public JsonConverterLong()
    {
        super("long");
    }

    @Override
    public Long convert(JsonElement element, String... args)
    {
        if (element == null || !element.isJsonPrimitive())
        {
            throw new IllegalArgumentException("JsonConverterLong: Invalid argument >> " + element);
        }
        return element.getAsLong();
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if (data instanceof Number)
        {
            return new JsonPrimitive(((Number) data).longValue());
        }
        return null;
    }
}
