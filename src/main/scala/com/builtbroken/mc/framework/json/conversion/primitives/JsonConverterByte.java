package com.builtbroken.mc.framework.json.conversion.primitives;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonElement;

/**
 * Wrapper for converting JSON element to string
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonConverterByte extends JsonConverter<Byte>
{
    public JsonConverterByte()
    {
        super("byte");
    }

    @Override
    public Byte convert(JsonElement element, String... args)
    {
        if (element == null || !element.isJsonPrimitive())
        {
            throw new IllegalArgumentException("JsonConverterByte: Invalid argument >> " + element);
        }
        return element.getAsByte();
    }
}
