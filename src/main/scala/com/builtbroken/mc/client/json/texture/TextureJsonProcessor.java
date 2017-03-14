package com.builtbroken.mc.client.json.texture;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/22/2016.
 */
public class TextureJsonProcessor extends JsonProcessor<TextureData>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "texture";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public TextureData process(JsonElement element)
    {
        final JsonObject object = element.getAsJsonObject();
        ensureValuesExist(object, "domain", "name", "key", "type");

        String domain = object.get("domain").getAsString();
        String name = object.get("name").getAsString();
        String key = object.get("key").getAsString();
        String typeKey = object.get("type").getAsString();

        TextureData.Type type = TextureData.Type.get(typeKey);
        if (type == null)
        {
            throw new IllegalArgumentException("Invalid texture type while loading " + element);
        }
        return new TextureData(this, key, domain, name, type);
    }
}
