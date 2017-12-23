package com.builtbroken.mc.framework.json.conversion;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * Applied to handlers that are able to convert data between objects and JSON
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/23/2017.
 */
public interface IJsonConverter<I>
{
    /**
     * Called to convert the JSON element into an object representing the data
     *
     * @param element - element that was marked with the type
     * @param args    - array of arguments to use during conversion, can be empty or null
     * @return object, or null if can't convert
     */
    I convert(JsonElement element, String... args);

    /**
     * Called to convert an object into JSON data
     *
     * @param type - data type, if you support several types make sure to match
     * @param data - object itself
     * @param args - array of arguments to use during conversion, can be empty or null
     * @return json element, or null if it can't be converted
     */
    JsonElement build(String type, Object data, String... args); //TODO throw exception for inability to handle type or convert

    /**
     * Keys used to ID this converter when matching data type.
     * this is not the key in the json data
     * think data type (int, float, double)
     *
     * @return a valid list containing at least 1 entry
     */
    List<String> getKeys();
}
