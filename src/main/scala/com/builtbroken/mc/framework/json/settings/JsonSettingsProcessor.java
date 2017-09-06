package com.builtbroken.mc.framework.json.settings;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.json.settings.data.JsonSettingInteger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Handles loading settings for the engine and it's content. Allows for these settings to be created inside of a configs and applied at runtime.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class JsonSettingsProcessor extends JsonProcessor<JsonSettingData>
{
    public final JsonSettingsProcessor INSTANCE = new JsonSettingsProcessor();

    @Override
    public JsonSettingData process(JsonElement element)
    {
        JsonObject object = element.getAsJsonObject();
        ensureValuesExist(object, "key", "type", "value");

        String key = object.getAsJsonPrimitive("key").getAsString();
        String type = object.getAsJsonPrimitive("type").getAsString();
        JsonPrimitive value = object.getAsJsonPrimitive("value");

        JsonSettingData settingData = null;

        if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer"))
        {
            settingData = new JsonSettingInteger(this, key, value.getAsInt());
        }

        //TODO json inject data

        return settingData;
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "setting";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }
}
