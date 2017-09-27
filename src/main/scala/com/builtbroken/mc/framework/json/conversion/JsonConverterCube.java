package com.builtbroken.mc.framework.json.conversion;

import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Used to convert json data to pos objects
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2017.
 */
public class JsonConverterCube extends JsonConverter<Cube>
{
    public JsonConverterCube()
    {
        super("cube");
    }

    @Override
    public Cube convert(JsonElement element, String... args)
    {
        return fromJson(element);
    }

    public static Cube fromJson(JsonElement element)
    {
        if (element instanceof JsonObject)
        {
            return fromJsonObject((JsonObject) element);
        }
        return null;
    }

    public static Cube fromJsonObject(JsonObject object)
    {
        JsonProcessor.ensureValuesExist(object, "min", "max");
        Pos min = JsonConverterPos.fromJson(object.get("min"));
        Pos max = JsonConverterPos.fromJson(object.get("max"));
        return new Cube(min, max);
    }
}
