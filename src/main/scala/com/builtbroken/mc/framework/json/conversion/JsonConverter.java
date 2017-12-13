package com.builtbroken.mc.framework.json.conversion;

import com.builtbroken.mc.framework.json.loading.JsonLoader;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

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
     * Keys used to ID this converter when matching data type.
     * this is not the key in the json data, think (int, float, double)
     */
    public final List<String> keys;

    public JsonConverter(String string, String... extraKeys)
    {
        this.keys = new ArrayList();
        this.keys.add(string);
        if (extraKeys != null)
        {
            for (String key : extraKeys)
            {
                this.keys.add(key);
            }
        }
    }


    /**
     * Called to convert the json element into
     * an object.
     *
     * @param element - element that was marked with the type
     * @param args    - array of arguments to use during conversion
     * @return object, or null if can't convert
     */
    public abstract I convert(JsonElement element, String... args);


    /**
     * Wrapper for calling {@link JsonLoader#convertElement(String, JsonElement, String...)}
     *
     * @param type - type (int, double, pos, block, item, etc)
     * @param data - json to convert
     * @param args - arguments to pass into the converter, see each converter for usage
     * @return object generated from JSON data
     * @throws Exception if data is invalid for the conversion type
     */
    public static Object convertElement(String type, JsonElement data, String... args)
    {
        return JsonLoader.convertElement(type, data, args);
    }

    public abstract JsonElement build(String type, Object data, String... args);
}
