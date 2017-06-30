package com.builtbroken.mc.lib.json.conversion;

import com.builtbroken.mc.imp.transform.vector.Pos;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Used to convert json data to pos objects
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2017.
 */
public class JsonConverterPos extends JsonConverter<Pos>
{
    public JsonConverterPos()
    {
        super("pos");
    }

    @Override
    public Pos convert(JsonElement element)
    {
        return fromJson(element);
    }

    public static Pos fromJson(JsonElement element)
    {
        if (element instanceof JsonObject)
        {
            return fromJsonObject((JsonObject) element);
        }
        else if (element instanceof JsonArray)
        {
            return fromJsonArray((JsonArray) element);
        }
        else if (element instanceof JsonPrimitive)
        {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber())
            {
                return new Pos(primitive.getAsDouble());
            }
            else if(primitive.isString())
            {
                return new Pos(Double.parseDouble(primitive.getAsString()));
            }
        }
        return null;
    }

    /**
     * Creates a pos from a json object containing x, y, z fields
     *
     * @param offsetObject
     * @return
     */
    public static Pos fromJsonObject(JsonObject offsetObject)
    {
        double x = 0, y = 0, z = 0;
        if (offsetObject.has("x"))
        {
            x = getNumber(offsetObject.getAsJsonPrimitive("x"));
        }
        if (offsetObject.has("y"))
        {
            y = getNumber(offsetObject.getAsJsonPrimitive("y"));
        }
        if (offsetObject.has("z"))
        {
            z = getNumber(offsetObject.getAsJsonPrimitive("z"));
        }
        return new Pos(x, y, z);
    }

    public static double getNumber(JsonPrimitive primitive)
    {
        if (primitive.isNumber())
        {
            return primitive.getAsDouble();
        }
        else if(primitive.isString())
        {
            return Double.parseDouble(primitive.getAsString());
        }
        else
        {
            throw new IllegalArgumentException("Could not parse value, " + primitive);
        }
    }


    /**
     * Creates a pos from a json array containing x, y, z fields
     *
     * @param offsetObject
     * @return
     */
    public static Pos fromJsonArray(JsonArray offsetObject)
    {
        double x = 0, y = 0, z = 0;

        JsonElement one = offsetObject.get(0);
        if (one.isJsonPrimitive())
        {
            JsonPrimitive p = one.getAsJsonPrimitive();
            if (p.isNumber())
            {
                x = one.getAsDouble();
            }
            else if (p.isString())
            {
                //TODO parse
                throw new IllegalStateException("Loading json array using strings is not supported yet for pos conversion. Data: " + offsetObject);
            }
        }
        JsonElement two = offsetObject.get(1);
        if (two.isJsonPrimitive())
        {
            JsonPrimitive p = two.getAsJsonPrimitive();
            if (p.isNumber())
            {
                y = two.getAsDouble();
            }
            else if (p.isString())
            {
                //TODO parse
                throw new IllegalStateException("Loading json array using strings is not supported yet for pos conversion. Data: " + offsetObject);
            }
        }
        JsonElement there = offsetObject.get(2);
        if (there.isJsonPrimitive())
        {
            JsonPrimitive p = there.getAsJsonPrimitive();
            if (p.isNumber())
            {
                x = there.getAsDouble();
            }
            else if (p.isString())
            {
                //TODO parse
                throw new IllegalStateException("Loading json array using strings is not supported yet for pos conversion. Data: " + offsetObject);
            }
        }
        return new Pos(x, y, z);
    }
}
