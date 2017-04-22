package com.builtbroken.mc.lib.json.processors.event;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Used to append orenames to exist blocks and items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonMissingMapEventProcessor extends JsonProcessor
{
    public static final String KEY = "missingMappingEvent";

    public static HashMap<String, String> mappings = new HashMap();

    @Override
    public IJsonGenObject process(JsonElement element)
    {
        JsonObject data = element.getAsJsonObject();
        JsonProcessor.ensureValuesExist(data, "oldValue", "newValue");
        String oldValue = data.get("oldValue").getAsString().trim();
        String newValue = data.get("newValue").getAsString().trim();

        if (!oldValue.isEmpty() && !newValue.isEmpty())
        {
            if (oldValue.contains(":") && newValue.contains(":"))
            {
                mappings.put(oldValue, newValue);
            }
            else
            {
                throw new IllegalArgumentException("Old and New value must contain : noting 'domain:name' ex: 'icbm:silo'");
            }
        }
        else
        {
            throw new IllegalArgumentException("Old and New value for missing mapping can not be empty");
        }

        return null;
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return KEY;
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }
}
