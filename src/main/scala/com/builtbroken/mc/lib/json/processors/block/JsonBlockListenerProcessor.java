package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.api.tile.listeners.ITileEventListener;
import com.builtbroken.mc.api.tile.listeners.ITileEventListenerBuilder;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.loading.JsonProcessorInjectionMap;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to append orenames to exist blocks and items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonBlockListenerProcessor extends JsonBlockSubProcessor
{
    public static final String KEY = "listeners";
    public static HashMap<String, ITileEventListenerBuilder> builders = new HashMap();
    private static HashMap<Class, JsonProcessorInjectionMap> injectionMaps = new HashMap();

    public static void addBuilder(ITileEventListenerBuilder builder)
    {
        builders.put(builder.getListenerKey().toLowerCase(), builder);
    }

    @Override
    public void process(BlockBase block, JsonElement arrayElement, List<IJsonGenObject> objectList)
    {
        JsonArray array = arrayElement.getAsJsonArray();
        for (JsonElement element : array)
        {
            String key = null;
            JsonObject data = null;
            if (element.isJsonPrimitive())
            {
                key = element.getAsString().toLowerCase();
            }
            else if (element.isJsonObject())
            {
                JsonObject object = element.getAsJsonObject();
                JsonProcessor.ensureValuesExist(object, "id");
                key = object.get("id").getAsString().toLowerCase();
                data = object;
            }

            if (builders.containsKey(key))
            {
                ITileEventListener listener = builders.get(key).createListener(block);
                if (listener != null)
                {
                    if (data != null)
                    {
                        if (!injectionMaps.containsKey(listener.getClass()))
                        {
                            injectionMaps.put(listener.getClass(), new JsonProcessorInjectionMap(listener.getClass()));
                        }
                        JsonProcessorInjectionMap injectionMap = injectionMaps.get(listener.getClass());
                        for (Map.Entry<String, JsonElement> entry : data.entrySet())
                        {
                            injectionMap.handle(listener, entry.getKey(), entry.getValue());
                        }
                    }
                    block.addListener(listener);
                }
            }
        }
    }

    @Override
    public void process(MetaData meta, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        //TODO implement
        process(block, element, objectList);
    }
}
