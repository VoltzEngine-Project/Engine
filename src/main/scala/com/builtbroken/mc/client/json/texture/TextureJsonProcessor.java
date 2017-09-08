package com.builtbroken.mc.client.json.texture;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.json.struct.JsonForLoop;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        List<JsonObject> elements = new ArrayList();

        if (object.has("for"))
        {
            JsonForLoop.generateDataForLoop(object.getAsJsonObject("for"), elements, new HashMap(), 0);
        }
        else if (object.has("forEach"))
        {
            JsonForLoop.generateDataForEachLoop(object.getAsJsonObject("forEach"), elements, new HashMap(), 0);
        }

        if (!elements.isEmpty())
        {
            for (JsonObject o : elements)
            {
                entries.add(handle(o));
            }
        }
        else
        {
            entries.add(handle(object));
        }
        return true;
    }

    /**
     * Handles loading a texture instance from JSON
     *
     * @param object
     * @return
     */
    protected TextureData handle(JsonObject object)
    {
        //IF changed update RenderJsonSubProcessor
        ensureValuesExist(object, "domain", "name", "key", "type");

        String domain = object.get("domain").getAsString();
        String name = object.get("name").getAsString();
        String key = object.get("key").getAsString();

        //Get texture type
        String typeKey = object.get("type").getAsString();
        TextureData.Type type = TextureData.Type.get(typeKey);
        if (type == null)
        {
            throw new IllegalArgumentException("Invalid texture type '" + typeKey + "' while loading ");
        }
        return new TextureData(this, key, domain, name, type);
    }
}
