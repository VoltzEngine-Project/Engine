package com.builtbroken.mc.prefab.json.block.processor;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import com.builtbroken.mc.prefab.json.block.BlockJson;
import com.builtbroken.mc.prefab.json.block.meta.BlockJsonMeta;
import com.builtbroken.mc.prefab.json.block.meta.MetaData;
import com.builtbroken.mc.prefab.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Load generic block data from a json
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonBlockProcessor extends JsonProcessor<BlockJson>
{
    /** Map of processors to run on unknown json object entries, used to process recipes and registry calls */
    public static final HashMap<String, JsonBlockSubProcessor> subProcessors = new HashMap();

    @Override
    public boolean canProcess(JsonElement element)
    {
        return element.isJsonObject() && element.getAsJsonObject().has("blockData");
    }

    @Override
    public BlockJson process(JsonElement element)
    {
        JsonObject object = element.getAsJsonObject();
        JsonObject blockData = object.get("blockData").getAsJsonObject();
        if (blockData.has("name") && blockData.has("material"))
        {
            BlockJson block;
            if (object.has("subtypes"))
            {
                block = new BlockJsonMeta(blockData.get("name").getAsString(), blockData.get("material").getAsString());
                readMeta((BlockJsonMeta) block, object.get("subtypes").getAsJsonArray());
            }
            else
            {
                block = new BlockJson(blockData.get("name").getAsString(), blockData.get("material").getAsString());
            }
            return block;
        }
        else
        {
            throw new IllegalArgumentException("JsonBlockProcessor: BlockData requires a name and a material value");
        }
    }

    /**
     * Reads the meta data stored in the subtypes entry
     *
     * @param block - meta block, unregistered
     * @param array - array of subtypes
     */
    public void readMeta(BlockJsonMeta block, JsonArray array)
    {
        //Loop every entry in the array, each entry should be meta values
        for (int i = 0; i < array.size() && i < 16; i++)
        {
            JsonObject json = array.get(i).getAsJsonObject();
            MetaData meta = new MetaData();
            int m = readMetaEntry(block, meta, json);
            if (m != -1)
            {
                //Meta is locked to 0-15
                if (m >= 0 && m < 16)
                {
                    if (block.meta[m] == null)
                    {
                        meta.index = m;
                        block.meta[m] = meta;
                    }
                    else
                    {
                        throw new IllegalArgumentException("JsonBlockProcessor: Meta value[" + m + "] was overridden inside the same file for block " + block.name);
                    }
                }
                else
                {
                    throw new IllegalArgumentException("JsonBlockProcessor: Meta values are restricted from 0 to 15");
                }
            }
            else
            {
                throw new IllegalArgumentException("JsonBlockProcessor: Each meta entry requires the value 'meta':int");
            }
        }
    }

    /**
     * Reads data from json into
     *
     * @param block
     * @param data
     * @param json
     * @return
     */
    public int readMetaEntry(BlockJson block, MetaData data, JsonObject json)
    {
        int meta = -1;
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            if (entry.getKey().equalsIgnoreCase("localization"))
            {
                data.localization = entry.getValue().getAsString();
            }
            else if (entry.getKey().equalsIgnoreCase("oreName"))
            {
                data.addOreName(entry.getValue().getAsString());
            }
            else if (entry.getKey().equalsIgnoreCase("meta"))
            {
                meta = entry.getValue().getAsInt();
            }
            else
            {
                processUnknownEntry(entry.getKey(), entry.getValue(), block, data);
            }
        }
        return meta;
    }

    /**
     * Called to process an unknown entry stored in the json
     *
     * @param name    - name of the entry
     * @param element - entry data
     * @param block   - block being processed
     * @param data    - meta being processed, can be null if processing block only
     */
    public void processUnknownEntry(String name, JsonElement element, BlockJson block, MetaData data)
    {
        if (subProcessors.containsKey(name))
        {
            if (subProcessors.get(name).canProcess(element))
            {
                if (data != null)
                {
                    subProcessors.get(name).processMeta(data, block, element);
                }
                else
                {
                    subProcessors.get(name).process(block, element);
                }
            }
            else
            {
                //Ignore unknown entries for backwards compatibility TODO add config option to enforce all data is read
                Engine.logger().error("JsonBlockProcessor: Error processing data for block " + block.name + ", processor rejected entry[" + name + "]=" + element);
            }
        }
        else
        {
            //Ignore unknown entries for backwards compatibility TODO add config option to enforce all data is read
            Engine.logger().error("JsonBlockProcessor: Error processing data for block " + block.name + ", no processor found for entry[" + name + "]=" + element);
        }
    }

    public void addSubProcessor(String entryName, JsonBlockSubProcessor processor)
    {
        if (subProcessors.containsKey(entryName) && subProcessors.get(entryName) != null)
        {
            //TODO add more data to error report
            //TODO ensure all processors have a toString() method
            Engine.logger().error("JsonBlockProcessor: Error sub process " + entryName + " is being overridden by " + processor);
        }
        subProcessors.put(entryName, processor);
        if (subProcessors instanceof ILoadable)
        {
            //TODO check if should load
            Engine.instance.loader.applyModule((ILoadable) subProcessors);
        }
    }
}
