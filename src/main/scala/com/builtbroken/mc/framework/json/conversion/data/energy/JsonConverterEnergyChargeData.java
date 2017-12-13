package com.builtbroken.mc.framework.json.conversion.data.energy;

import com.builtbroken.mc.api.data.energy.IEnergyChargeData;
import com.builtbroken.mc.framework.energy.data.EnergyChargeData;
import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/7/2017.
 */
public class JsonConverterEnergyChargeData extends JsonConverter<IEnergyChargeData>
{
    public JsonConverterEnergyChargeData()
    {
        super("IEnergyChargeData");
    }

    @Override
    public IEnergyChargeData convert(JsonElement element, String... args)
    {
        return fromJson(element);
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if (data instanceof EnergyChargeData)
        {
            JsonObject object = new JsonObject();
            object.add("input", new JsonPrimitive(((EnergyChargeData) data).getInputEnergyLimit()));
            object.add("output", new JsonPrimitive(((EnergyChargeData) data).getOutputEnergyLimit()));
            return object;
        }
        return null;
    }

    public static IEnergyChargeData fromJson(JsonElement element)
    {
        if (element instanceof JsonObject)
        {
            return fromJsonObject((JsonObject) element);
        }
        return null;
    }

    public static IEnergyChargeData fromJsonObject(JsonObject object)
    {
        JsonProcessor.ensureValuesExist(object, "input", "output");
        int in = object.getAsJsonPrimitive("input").getAsInt();
        int out = object.getAsJsonPrimitive("output").getAsInt();
        return new EnergyChargeData().setInputEnergyLimit(in).setOutputEnergyLimit(out);
    }
}
