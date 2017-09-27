package com.builtbroken.mc.framework.json.conversion.data.mc;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.data.JsonBlockEntry;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonConverterBlock extends JsonConverter<JsonBlockEntry>
{
    @Override
    public JsonBlockEntry convert(JsonElement element, String... args)
    {
        if(element.isJsonPrimitive())
        {
            return new JsonBlockEntry(element.getAsString());
        }
        throw new IllegalArgumentException("JsonConverterBlock: could not convert json to block reference, json: " + element);
    }
}
