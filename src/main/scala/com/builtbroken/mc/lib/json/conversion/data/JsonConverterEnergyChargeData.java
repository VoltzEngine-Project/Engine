package com.builtbroken.mc.lib.json.conversion.data;

import com.builtbroken.mc.api.data.energy.IEnergyChargeData;
import com.builtbroken.mc.lib.json.conversion.JsonConverter;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.builtbroken.mc.prefab.energy.EnergyChargeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    public IEnergyChargeData convert(JsonElement element)
    {
        return fromJson(element);
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
