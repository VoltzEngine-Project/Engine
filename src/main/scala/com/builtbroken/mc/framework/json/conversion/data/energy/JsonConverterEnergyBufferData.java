package com.builtbroken.mc.framework.json.conversion.data.energy;

import com.builtbroken.mc.api.data.energy.IEnergyBufferData;
import com.builtbroken.mc.framework.energy.data.EnergyBufferData;
import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/7/2017.
 */
public class JsonConverterEnergyBufferData extends JsonConverter<IEnergyBufferData>
{
    public JsonConverterEnergyBufferData()
    {
        super("IEnergyBufferData");
    }

    @Override
    public IEnergyBufferData convert(JsonElement element, String... args)
    {
        return fromJson(element);
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if (data instanceof EnergyBufferData)
        {
            JsonObject object = new JsonObject();
            object.add("power", new JsonPrimitive(((EnergyBufferData) data).getEnergyCapacity()));
            return object;
        }
        return null;
    }

    public static IEnergyBufferData fromJson(JsonElement element)
    {
        if (element instanceof JsonObject)
        {
            return fromJsonObject((JsonObject) element);
        }
        return null;
    }

    public static IEnergyBufferData fromJsonObject(JsonObject object)
    {
        JsonProcessor.ensureValuesExist(object, "power");
        int in = object.getAsJsonPrimitive("power").getAsInt();
        return new EnergyBufferData().setEnergyCapacity(in);
    }
}
