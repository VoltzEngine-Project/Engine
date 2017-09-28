package com.builtbroken.mc.framework.json.conversion.structures.list;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used to convert json data to a list of objects which are run through the converter as well
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/28/2017.
 */
public class JsonConverterList extends JsonConverter<List>
{
    public JsonConverterList()
    {
        super("list", "list.array");
    }

    @Override
    public List convert(JsonElement element, String[] args)
    {
        List list = new ArrayList();
        if (args.length != 1)
        {
            throw new RuntimeException("JsonConverterList: arguments needs to contain at least 1 value containing of conversion type in order to function");
        }
        if (element.isJsonArray())
        {
            final String type = args[0];

            for (JsonElement e : element.getAsJsonArray())
            {
                Object object = convertElement(type, e);  //TODO args?
                if (object == null)
                {
                    throw new RuntimeException("JsonConverterList: failed to convert data '" + e + "' to type '" + type + "'");
                }
                if(object instanceof Collection)
                {
                    list.addAll((Collection) object);
                }
                else
                {
                    list.add(object);
                }
            }
        }
        else
        {
            throw new RuntimeException("JsonConverterHashMap: json element needs to be an array in order to convert to hash map");
        }
        return list;
    }

}
