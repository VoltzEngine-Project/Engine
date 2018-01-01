package com.builtbroken.mc.client.json.effects;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles loading audio data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017
 */
public class EffectListJsonProcessor extends JsonProcessor<EffectList>
{
    public EffectListJsonProcessor()
    {
        super(EffectList.class);
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "effects";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public EffectList process(JsonElement element)
    {
        final JsonObject effectDataJson = element.getAsJsonObject();
        ensureValuesExist(effectDataJson, "key", "list");

        String key = effectDataJson.get("key").getAsString().toLowerCase();

        EffectList data = new EffectList(this, key);

        JsonArray list = effectDataJson.getAsJsonArray("list");
        for(JsonElement entry : list)
        {
            JsonObject object = entry.getAsJsonObject();
            if(object.has("list"))
            {
                data.layers.add(process(object));
            }
            else
            {
                data.layers.add(EffectJsonProcessor.INSTANCE.process(object));
            }
        }
        return data;
    }
}
