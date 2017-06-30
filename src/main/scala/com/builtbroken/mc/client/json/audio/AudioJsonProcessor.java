package com.builtbroken.mc.client.json.audio;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles loading audio data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017
 */
public class AudioJsonProcessor extends JsonProcessor<AudioData>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "audio";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public AudioData process(JsonElement element)
    {
        final JsonObject object = element.getAsJsonObject();
        ensureValuesExist(object, "domain", "name", "key");

        String domain = object.get("domain").getAsString();
        String name = object.get("name").getAsString();
        String key = object.get("key").getAsString().toLowerCase();

        return new AudioData(this, key, domain + ":" + name);
    }
}
