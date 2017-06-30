package com.builtbroken.mc.client.json.texture;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.Map;

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
    public boolean process(JsonElement element, List<IJsonGenObject> entries)
    {
        JsonObject object = element.getAsJsonObject();
        if (object.has("for"))
        {
            object = object.getAsJsonObject("for");
            ensureValuesExist(object, "start", "end", "state");
            int start = object.getAsJsonPrimitive("start").getAsInt();
            int end = object.getAsJsonPrimitive("end").getAsInt();

            if (start >= end)
            {
                throw new IllegalArgumentException("Start can not be greater than or equal to end for a for loop.");
            }

            JsonObject template = object.getAsJsonObject("state");
            for (int i = start; i <= end; i++)
            {
                JsonObject state = new JsonObject();

                //Copy template and rename values as needed
                for (Map.Entry<String, JsonElement> entry : template.entrySet())
                {
                    if (entry.getValue() instanceof JsonPrimitive && ((JsonPrimitive) entry.getValue()).isString())
                    {
                        String s = entry.getValue().getAsString();
                        s = s.replace("%number%", "" + i);
                        state.add(entry.getKey(), new JsonPrimitive(s));
                    }
                    else
                    {
                        state.add(entry.getKey(), entry.getValue());
                    }
                }

                //Load state
                entries.add(handle(state));
            }
        }
        else
        {
            entries.add(handle(object));
        }
        return true;
    }

    protected TextureData handle(JsonObject object)
    {
        ensureValuesExist(object, "domain", "name", "key", "type");

        String domain = object.get("domain").getAsString();
        String name = object.get("name").getAsString();
        String key = object.get("key").getAsString();
        String typeKey = object.get("type").getAsString();

        TextureData.Type type = TextureData.Type.get(typeKey);
        if (type == null)
        {
            throw new IllegalArgumentException("Invalid texture type '" + typeKey + "' while loading ");
        }
        return new TextureData(this, key, domain, name, type);
    }
}
