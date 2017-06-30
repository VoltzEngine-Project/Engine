package com.builtbroken.mc.lib.json.override;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2017.
 */
public class JsonOverrideProcessor extends JsonProcessor<JsonOverrideData>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "override";
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
        ensureValuesExist(data, "contentID", "processorID", "action", "data");
        return new JsonOverrideData(this, data.get("contentID").getAsString(), data.get("processorID").getAsString(), data.get("action").getAsString(), data.get("data"));
    }
}
