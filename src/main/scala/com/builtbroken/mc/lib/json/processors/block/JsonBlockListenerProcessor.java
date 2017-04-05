package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.api.tile.listeners.ITileEventListener;
import com.builtbroken.mc.api.tile.listeners.ITileEventListenerBuilder;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;

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

    public static void addBuilder(ITileEventListenerBuilder builder)
    {
        builders.put(builder.getListenerKey(), builder);
    }

    @Override
    public void process(BlockBase block, JsonElement arrayElement, List<IJsonGenObject> objectList)
    {
        JsonArray array = arrayElement.getAsJsonArray();
        for (JsonElement element : array)
        {
            if (element.isJsonPrimitive())
            {
                String key = element.getAsString().toLowerCase();
                if (builders.containsKey(key))
                {
                    ITileEventListener listener = builders.get(key).createListener(block);
                    if (listener != null)
                    {
                        block.addListener(listener);
                    }
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
