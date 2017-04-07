package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.jlib.lang.DebugPrinter;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.BlockPropertyData;
import com.builtbroken.mc.framework.block.meta.BlockMeta;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.loading.JsonProcessorInjectionMap;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load generic block data from a json
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonBlockProcessor extends JsonProcessor<BlockBase>
{
    public static final String KEY = "block";
    /** Map of processors to run on unknown json object entries, used to process recipes and registry calls */
    public final HashMap<String, IJsonBlockSubProcessor> subProcessors = new HashMap();

    /** Keeps track of json fields that are used for block data directly and can not be used by sub processors */
    public final List<String> blockFields = new ArrayList();

    protected final JsonProcessorInjectionMap keyHandler;

    protected final DebugPrinter debugPrinter;

    public JsonBlockProcessor()
    {
        keyHandler = new JsonProcessorInjectionMap(BlockPropertyData.class);
        debugPrinter = JsonContentLoader.INSTANCE.debug;
        //Field entries to prevent sub processors firing
        // each entry need to be lower cased to work
        blockFields.add("id");
        blockFields.add("name");
        blockFields.add("mod");
        blockFields.add("subtypes");
        blockFields.add("material");
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

    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> objectList)
    {
        debugPrinter.start("BlockProcessor", "Processing entry");
        //Get object and ensure minimal keys exist
        JsonObject blockData = element.getAsJsonObject();
        ensureValuesExist(blockData, "name", "id", "mod");

        //Load default data
        String mod = blockData.getAsJsonPrimitive("mod").getAsString();
        String id = blockData.getAsJsonPrimitive("id").getAsString();
        String name = blockData.get("name").getAsString();

        debugPrinter.log("Name: " + name);
        debugPrinter.log("Mod: " + mod);
        debugPrinter.log("ID: " + id);

        //Generate object
        BlockPropertyData blockPropertyData = new BlockPropertyData(this, id, mod, name);

        //Load blocks
        BlockBase block;
        //Meta data loading
        if (blockData.has("subtypes"))
        {
            block = new BlockMeta(blockPropertyData);
            //Call to load metadata
            readMeta((BlockMeta) block, blockData.get("subtypes").getAsJsonArray(), objectList);
        }
        //No meta data
        else
        {
            block = new BlockBase(blockPropertyData);
        }

        //Call to process extra tags from file
        for (Map.Entry<String, JsonElement> entry : blockData.entrySet())
        {
            if (keyHandler.handle(entry.getKey().toLowerCase(), entry.getValue()))
            {
                if (Engine.runningAsDev)
                {
                    debugPrinter.log("Injected Key: " + entry.getKey());
                }
            }
            else if (!blockFields.contains(entry.getKey().toLowerCase()))
            {
                processUnknownEntry(entry.getKey(), entry.getValue(), block, null, objectList);
            }
        }

        //Add block to object list
        objectList.add(block);

        debugPrinter.end("Done...");
        return true;
    }

    /**
     * Reads the meta data stored in the subtypes entry
     *
     * @param block - meta block, unregistered
     * @param array - array of subtypes
     */
    public void readMeta(BlockMeta block, JsonArray array, List<IJsonGenObject> objectList)
    {
        //Loop every entry in the array, each entry should be meta values
        for (int i = 0; i < array.size() && i < 16; i++)
        {
            JsonObject json = array.get(i).getAsJsonObject();
            MetaData meta = new MetaData();

            //Reads the meta entry and then returns the meta to assign
            int m = readMetaEntry(block, meta, json, objectList);

            //Meta of -1 is invalid
            if (m != -1)
            {
                //Meta is locked to 0-15
                if (m >= 0 && m < 16)
                {
                    //Prevent overriding by mistake
                    if (block.meta[m] == null)
                    {
                        meta.index = m;
                        block.meta[m] = meta;
                    }
                    else
                    {
                        throw new IllegalArgumentException("JsonBlockProcessor: Meta value[" + m + "] was overridden inside the same file for block " + block.data.name);
                    }
                }
                else
                {
                    throw new IllegalArgumentException("JsonBlockProcessor: Meta values are restricted from 0 to 15");
                }
            }
            else
            {
                throw new IllegalArgumentException("JsonBlockProcessor: Each meta entry requires the value 'meta' of type Integer");
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
    public int readMetaEntry(BlockBase block, MetaData data, JsonObject json, List<IJsonGenObject> objectList)
    {
        int meta = -1;
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            if (entry.getKey().equalsIgnoreCase("localization"))
            {
                data.localization = entry.getValue().getAsString();
            }
            else if (entry.getKey().equalsIgnoreCase("meta"))
            {
                meta = entry.getValue().getAsInt();
            }
            else
            {
                processUnknownEntry(entry.getKey(), entry.getValue(), block, data, objectList);
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
    public void processUnknownEntry(String name, JsonElement element, BlockBase block, MetaData data, List<IJsonGenObject> objectList)
    {
        if (subProcessors.containsKey(name))
        {
            if (subProcessors.get(name).canProcess(name, element))
            {
                if (data != null)
                {
                    subProcessors.get(name).process(data, block, element, objectList);
                }
                else
                {
                    subProcessors.get(name).process(block, element, objectList);
                }
            }
            else
            {
                //Ignore unknown entries for backwards compatibility TODO add config option to enforce all data is read
                Engine.logger().error("JsonBlockProcessor: Error processing data for block " + block.data.name + ", processor rejected entry[" + name + "]=" + element);
            }
        }
        else
        {
            //Ignore unknown entries for backwards compatibility TODO add config option to enforce all data is read
            Engine.logger().error("JsonBlockProcessor: Error processing data for block " + block.data.name + ", no processor found for entry[" + name + "]=" + element);
        }
    }

    public void addSubProcessor(String entryName, IJsonBlockSubProcessor processor)
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
