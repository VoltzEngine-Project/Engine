package com.builtbroken.mc.framework.json.conversion.structures.list;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonElement;

import java.util.HashMap;

/**
 * Used to convert json data to pos objects
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2017.
 */
public class JsonConverterList extends JsonConverter<HashMap>
{
    public JsonConverterList()
    {
        super("list", "list.array");
    }

    @Override
    public HashMap convert(JsonElement element, String[] args)
    {
        HashMap map = new HashMap();
        if (args.length != 1)
        {
            throw new RuntimeException("JsonConverterList: arguments needs to contain at least 1 value containing of conversion type in order to function");
        }
        if (element.isJsonArray())
        {
            final String type = args[0];

            for (JsonElement e : element.getAsJsonArray())
            {
                Object keyObject = convertElement(type, e);  //TODO args?
                if (keyObject == null)
                {
                    throw new RuntimeException("JsonConverterList: failed to convert key data '" + e + "' to type '" + type + "'");
                }
            }
        }
        else
        {
            throw new RuntimeException("JsonConverterHashMap: json element needs to be an array in order to convert to hash map");
        }
        return map;
    }

}
