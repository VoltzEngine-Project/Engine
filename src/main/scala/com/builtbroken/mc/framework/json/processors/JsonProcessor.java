package com.builtbroken.mc.framework.json.processors;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.conversion.JsonConverterNBT;
import com.builtbroken.mc.framework.json.data.JsonItemEntry;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorInjectionMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.Loader;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Map;

/**
 * Default implementation for processor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public abstract class JsonProcessor<D extends IJsonGenObject> implements IJsonProcessor
{
    protected JsonProcessorInjectionMap keyHandler;
    protected DebugPrinter debugPrinter;

    public JsonProcessor()
    {
        debugPrinter = JsonContentLoader.INSTANCE != null ? JsonContentLoader.INSTANCE.debug : new DebugPrinter(LogManager.getLogger());
    }

    public JsonProcessor(Class<D> clazz)
    {
        this();
        keyHandler = new JsonProcessorInjectionMap(clazz);
    }

    @Override
    public boolean canProcess(String key, JsonElement element)
    {
        return key.equalsIgnoreCase(getJsonKey());
    }

    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> entries)
    {
        D output = process(element);
        if (output != null)
        {
            entries.add(output);
        }
        return true;
    }

    public D process(JsonElement element)
    {
        return null;
    }

    protected void processAdditionalKeys(D objectToInject, JsonObject jsonData)
    {
        try
        {
            //Call to process extra tags from file
            for (Map.Entry<String, JsonElement> entry : jsonData.entrySet())
            {
                if (keyHandler.handle(objectToInject, entry.getKey().toLowerCase(), entry.getValue()))
                {
                    if (Engine.runningAsDev)
                    {
                        debugPrinter.log("Injected Key: " + entry.getKey());
                    }
                }
            }

            keyHandler.enforceRequired(objectToInject);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace(); //Technically can't happen
        }
    }

    /**
     * Quick way to check that required fields exist in the json file
     *
     * @param object
     * @param values
     */
    public static void ensureValuesExist(JsonObject object, String... values)
    {
        for (String value : values)
        {
            if (!object.has(value))
            {
                throw new IllegalArgumentException("File is missing " + value + " value " + object);
            }
        }
    }

    @Override
    public boolean shouldLoad(JsonElement object)
    {
        if (object instanceof JsonObject)
        {
            if (((JsonObject) object).has("loadCondition"))
            {
                final String type = ((JsonObject) object).getAsJsonPrimitive("loadCondition").getAsString();
                if (type.startsWith("mod@"))
                {
                    String modName = type.substring(4, type.length());
                    if (!Loader.isModLoaded(modName))
                    {
                        return false;
                    }
                }
                else if (type.equalsIgnoreCase("devMode"))
                {
                    return Engine.runningAsDev;
                }
            }
        }
        return true;
    }


    public static Object getItemFromJson(JsonElement element)
    {
        if (element.isJsonObject())
        {
            return fromJson(element.getAsJsonObject());
        }
        else if (element.isJsonPrimitive())
        {
            return element.getAsString();
        }
        throw new RuntimeException("Could not convert json element into item entry >> '" + element + "'");
    }

    public static JsonItemEntry fromJson(JsonObject itemStackObject)
    {
        //Convert and check types
        ensureValuesExist(itemStackObject, "item");

        //Create entry
        JsonItemEntry entry = new JsonItemEntry();

        //Get required data
        entry.item = itemStackObject.get("item").getAsString();
        if (itemStackObject.has("meta"))
        {
            entry.damage = itemStackObject.get("meta").getAsString();
        }
        else if (itemStackObject.has("damage"))
        {
            entry.damage = itemStackObject.get("damage").getAsString();
        }

        //Load optional stacksize
        if (itemStackObject.has("count"))
        {
            entry.count = itemStackObject.getAsJsonPrimitive("count").getAsInt();
            if (entry.count < 0)
            {
                throw new RuntimeException("Recipe output count must be above zero");
            }
            else if (entry.count > 64)
            {
                throw new RuntimeException("Recipe output count must be below 64 as this is the max stacksize for this version of Minecraft.");
            }
        }

        //Load optional item data
        if (itemStackObject.has("nbt"))
        {
            entry.nbt = JsonConverterNBT.handle(itemStackObject.get("nbt"));
            if (entry.nbt.hasNoTags())
            {
                entry.nbt = null;
            }
        }
        return entry;
    }
}
