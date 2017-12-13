package com.builtbroken.mc.framework.json.override;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.json.struct.JsonConditional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles loading {@link JsonOverrideData}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2017.
 */
public class JsonOverrideProcessor extends JsonProcessor<JsonOverrideData>
{
    public static final String JSON_CONTENT_KEY = "contentID";
    public static final String JSON_PROCESSOR_KEY = "processorID";
    public static final String JSON_ACTION_KEY = "action";
    public static final String JSON_DATA_KEY = "data";
    public static final String JSON_ENABLE_KEY = "enable";

    public static final String JSON_OVERRIDE_KEY = "override";

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return JSON_OVERRIDE_KEY;
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public JsonOverrideData process(JsonElement element)
    {
        JsonObject data = (JsonObject) element;
        ensureValuesExist(data, JSON_CONTENT_KEY, JSON_PROCESSOR_KEY, JSON_ACTION_KEY, JSON_DATA_KEY);
        if (data.has(JSON_ENABLE_KEY))
        {
            JsonElement loadCondition = data.get(JSON_ENABLE_KEY);
            if (!JsonConditional.isConditionalTrue(loadCondition, this))
            {
                return null;
            }
        }
        return new JsonOverrideData(this, data.get(JSON_CONTENT_KEY).getAsString(), data.get(JSON_PROCESSOR_KEY).getAsString(), data.get(JSON_ACTION_KEY).getAsString(), data.get(JSON_DATA_KEY));
    }
}
