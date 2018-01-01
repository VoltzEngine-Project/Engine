package com.builtbroken.mc.framework.json.processors;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.conversion.data.mc.JsonConverterItem;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorInjectionMap;
import com.builtbroken.mc.framework.json.struct.JsonConditional;
import com.builtbroken.mc.framework.json.struct.JsonForLoop;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    public boolean process(JsonElement element, List<IJsonGenObject> objects)
    {
        handle(null, element, objects);
        return true;
    }

    /**
     * Internal method for handling a new JSON entry. Mainly used to handle for-loops before
     * processing the main data. Not all processors use this system e.g. Block and Item processor
     * are intentionally set to not use for-loops to prevent item IDs from being consumed.
     *
     * @param out     - output object of the process, normally null however can be used to
     *                note an item, entity, block, etc. This way the json doesn't need to specify
     *                a target object to apply the data towards.
     * @param element - data
     * @param objects - list of processed objects from data
     */
    protected void handle(final Object out, JsonElement element, List<IJsonGenObject> objects)
    {
        //Handle for loops
        if (element.isJsonObject())
        {
            JsonObject jsonObject = element.getAsJsonObject();
            if (JsonForLoop.hasLoops(jsonObject))
            {
                JsonForLoop.handleLoops(jsonObject, o -> {
                    D data = process(out, o);
                    if (data != null)
                    {
                        processAdditionalKeys(data, o);
                        objects.add(data);
                    }
                });
                return;
            }
        }

        //Default handling
        D data = process(out, element);
        if (data != null)
        {
            if (element.isJsonObject())
            {
                processAdditionalKeys(data, element.getAsJsonObject());
            }
            objects.add(data);
        }
    }

    /**
     * Called to process a json element into an object. Its assumed
     * one element results in one data segement. If this is not the
     * case use {@link #process(JsonElement, List)} instead
     *
     * @param out     - host of the data (item, block, entity, etc), can be null
     *                if the json was not nested into a host object.
     * @param element - data
     * @return object
     */
    protected D process(final Object out, final JsonElement element)
    {
        return process(element);
    }

    /**
     * Called to process a json element into an object.  Its assumed
     * one element results in one data segement. If this is not the
     * case use {@link #process(JsonElement, List)} instead.
     * <p>
     * As well its assumed the host of the data doesn't matter. If this
     * is not the case use {@link #process(Object, JsonElement)} or another
     * method that provides access to the host object.
     *
     * @param element - data
     * @return object
     */
    protected D process(final JsonElement element)
    {
        return null;
    }

    /**
     * Called to process additional keys as annotation injection data
     *
     * @param objectToInject - object
     * @param jsonData       - data
     */
    protected void processAdditionalKeys(D objectToInject, JsonObject jsonData)
    {
        processAdditionalKeys(objectToInject, jsonData, null);
    }

    /**
     * Called to process additional keys as annotation injection data
     *
     * @param objectToInject - object
     * @param jsonData       - data
     * @param keysToIgnore   - list of keys to ignore from processing
     */
    protected void processAdditionalKeys(D objectToInject, JsonObject jsonData, List<String> keysToIgnore)
    {
        if(keyHandler != null)
        {
            try
            {
                //Call to process extra tags from file
                for (Map.Entry<String, JsonElement> entry : jsonData.entrySet())
                {
                    if ((keysToIgnore == null || !keysToIgnore.contains(entry.getKey()))
                            && keyHandler.handle(objectToInject, entry.getKey().toLowerCase(), entry.getValue()))
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
                throw new IllegalArgumentException("File is missing '" + value + "' value from inside '" + object + "'");
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
                return JsonConditional.isConditionalTrue(((JsonObject) object).get("loadCondition"), this);
            }
        }
        return true;
    }


    public static Object getItemFromJson(JsonElement element)
    {
        if (element.isJsonObject())
        {
            return JsonConverterItem.fromJson(element.getAsJsonObject());
        }
        else if (element.isJsonPrimitive())
        {
            return element.getAsString();
        }
        throw new RuntimeException("Could not convert json element into item entry >> '" + element + "'");
    }


}
