package com.builtbroken.mc.client.json.effects;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Handles loading audio data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017
 */
public class EffectJsonProcessor extends JsonProcessor<EffectData>
{
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
    public EffectData process(JsonElement element)
    {
        final JsonObject object = element.getAsJsonObject();
        ensureValuesExist(object, "effectID", "key");

        String effectID = object.get("effectID").getAsString();
        String key = object.get("key").getAsString().toLowerCase();

        EffectData data = new EffectData(this, key, effectID, new NBTTagCompound());
        if (object.has("useEndpoint"))
        {
            data.useEndPointForVelocity = object.get("useEndpoint").getAsBoolean();
        }
        return data;
    }
}
