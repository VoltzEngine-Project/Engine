package com.builtbroken.mc.framework.json.settings;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.loading.JsonProcessorInjectionMap;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.json.settings.data.JsonSettingBoolean;
import com.builtbroken.mc.framework.json.settings.data.JsonSettingInteger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles loading settings for the engine and it's content. Allows for these settings to be created inside of a configs and applied at runtime.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class JsonSettingsProcessor extends JsonProcessor<JsonSettingData>
{
    public static final JsonSettingsProcessor INSTANCE = new JsonSettingsProcessor();

    HashMap<Class<? extends JsonSettingData>, JsonProcessorInjectionMap> injectionMaps = new HashMap();

    @Override
    public JsonSettingData process(JsonElement element)
    {
        JsonObject jsonData = element.getAsJsonObject();
        ensureValuesExist(jsonData, "key", "type", "value");

        debugPrinter.start("SettingProcessor", "Processing entry", Engine.runningAsDev);

        String key = jsonData.getAsJsonPrimitive("key").getAsString();
        String type = jsonData.getAsJsonPrimitive("type").getAsString();
        JsonPrimitive value = jsonData.getAsJsonPrimitive("value");

        debugPrinter.log("Key: " + key);
        debugPrinter.log("Type: " + type);
        debugPrinter.log("Value: " + value);

        JsonSettingData settingData = null;

        if (type.equalsIgnoreCase("byte"))
        {

        }
        else if (type.equalsIgnoreCase("short"))
        {

        }
        else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer"))
        {
            settingData = new JsonSettingInteger(this, key, value.getAsInt());
        }
        else if (type.equalsIgnoreCase("long"))
        {

        }
        else if (type.equalsIgnoreCase("float"))
        {

        }
        else if (type.equalsIgnoreCase("double"))
        {

        }
        else if (type.equalsIgnoreCase("boolean"))
        {
            settingData = new JsonSettingBoolean(this, key, value.getAsBoolean());
        }
        else
        {
            //load string
        }

        if (settingData != null)
        {
            if (!injectionMaps.containsKey(settingData.getClass()))
            {
                injectionMaps.put(settingData.getClass(), new JsonProcessorInjectionMap(settingData.getClass()));
            }
            JsonProcessorInjectionMap injectionMap = injectionMaps.get(settingData.getClass());

            try
            {
                //Call to process extra tags from file
                for (Map.Entry<String, JsonElement> entry : jsonData.entrySet())
                {
                    if (injectionMap.handle(settingData, entry.getKey().toLowerCase(), entry.getValue()))
                    {
                        if (Engine.runningAsDev)
                        {
                            debugPrinter.log("Injected Key: " + entry.getKey());
                        }
                    }
                }

                injectionMap.enforceRequired(settingData);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace(); //Technically can't happen
            }
        }

        debugPrinter.end();
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
