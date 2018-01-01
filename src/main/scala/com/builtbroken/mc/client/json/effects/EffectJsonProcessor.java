package com.builtbroken.mc.client.json.effects;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles loading audio data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017
 */
public class EffectJsonProcessor extends JsonProcessor<EffectLayer>
{
    public static final EffectJsonProcessor INSTANCE = new EffectJsonProcessor();

    private EffectJsonProcessor()
    {
        super(EffectLayer.class);
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "effect";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public EffectLayer process(JsonElement element)
    {
        final JsonObject effectDataJson = element.getAsJsonObject();
        ensureValuesExist(effectDataJson, "effectID", "key");

        String effectID = effectDataJson.get("effectID").getAsString();
        String key = effectDataJson.get("key").getAsString().toLowerCase();

        return new EffectLayer(this, key, effectID);
    }
}
