package com.builtbroken.mc.framework.json.conversion.structures.arrays;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonConverterByteArray extends JsonConverter<byte[]>
{
    public JsonConverterByteArray()
    {
        super("array.byte");
    }

    @Override
    public byte[] convert(JsonElement element, String... args)
    {
        if (element instanceof JsonArray)
        {
            byte[] array = new byte[element.getAsJsonArray().size()];
            byte index = 0;
            for (JsonElement e : element.getAsJsonArray())
            {
                if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                {
                    array[index++] = e.getAsByte();
                }
                else
                {
                    throw new IllegalArgumentException("Can not convert object to byte array, as '" + e + "' is not an byte");
                }
            }

            return array;
        }
        return null;
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if(data instanceof Byte[])
        {
            JsonArray array = new JsonArray();
            for(byte b : (byte[])data)
            {
                array.add(new JsonPrimitive(b));
            }
            return array;
        }
        return null;
    }
}
