package com.builtbroken.mc.prefab.json.block.processor;

import com.builtbroken.mc.prefab.json.block.BlockJson;
import com.builtbroken.mc.prefab.json.block.meta.MetaData;
import com.google.gson.JsonElement;

/**
 * Used to process sub entries in a block file. Useful for handling recipes, registry calls, etc...
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class JsonBlockSubProcessor
{
    /**
     * Called to process a json block section
     *
     * @return
     */
    public boolean canProcess(JsonElement element)
    {
        return true;
    }

    /**
     * Called to process json data entry
     *
     * @param block   - block that is being process, keep in mind
     * @param element - data
     */
    public void process(BlockJson block, JsonElement element)
    {

    }

    /**
     * Called to process json data entry for meta data
     *
     * @param block   - block that is being process, keep in mind
     * @param element - data
     * @param data    - if the process was called on a meta entry this will contain the entry, Keep in
     *                mind it can be null for block only data and is not registered to the block
     */
    public void processMeta(MetaData data, BlockJson block, JsonElement element)
    {

    }
}
