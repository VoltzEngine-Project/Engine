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
public class JsonConverterString extends JsonConverter<String>
{
    public JsonConverterString()
    {
        super("string");
    }

    @Override
    public String convert(JsonElement element, String... args)
    {
        if (element == null || !element.isJsonPrimitive())
        {
            throw new IllegalArgumentException("JsonConverterString: Invalid argument >> " + element);
        }
        return element.getAsString();
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        return new JsonPrimitive(data != null ? data.toString() : "null");
    }
}
