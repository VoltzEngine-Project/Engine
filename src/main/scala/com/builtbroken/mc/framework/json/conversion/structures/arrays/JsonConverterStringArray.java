package com.builtbroken.mc.framework.json.conversion.structures.arrays;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/12/2017.
 */
public class JsonConverterStringArray extends JsonConverter<String[]>
{
    public JsonConverterStringArray()
    {
        super("array.string");
    }

    @Override
    public String[] convert(JsonElement element, String... args)
    {
        if (element instanceof JsonArray)
        {
            String[] array = new String[element.getAsJsonArray().size()];
            int index = 0;
            for (JsonElement e : element.getAsJsonArray())
            {
                if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString())
                {
                    array[index++] = e.getAsString().trim();
                }
                else
                {
                    throw new IllegalArgumentException("Can not convert object to string array, as '" + e + "' is not a string");
                }
            }

            return array;
        }
        return null;
    }
}
