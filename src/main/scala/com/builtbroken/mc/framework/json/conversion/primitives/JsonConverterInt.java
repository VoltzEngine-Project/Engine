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
public class JsonConverterInt extends JsonConverter<Integer>
{
    public JsonConverterInt()
    {
        super("int", "integer");
    }

    @Override
    public Integer convert(JsonElement element, String... args)
    {
        if (element == null || !element.isJsonPrimitive())
        {
            throw new IllegalArgumentException("JsonConverterInteger: Invalid argument >> " + element);
        }
        return element.getAsInt();
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if (data instanceof Number)
        {
            return new JsonPrimitive(((Number) data).intValue());
        }
        return null;
    }
}
