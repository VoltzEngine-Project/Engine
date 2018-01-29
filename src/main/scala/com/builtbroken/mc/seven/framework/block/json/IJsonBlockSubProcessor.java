package com.builtbroken.mc.seven.framework.block.json;

import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.meta.MetaData;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * Applied to processors that support loading data from inside of a block
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public interface IJsonBlockSubProcessor
{
    boolean canProcess(String key, JsonElement element);

    /**
     * Called to process json data entry
     *
     * @param block      - block that is being process, keep in mind
     * @param element    - data
     * @param objectList - list to add generated objects
     */
    void process(BlockBase block, JsonElement element, List<IJsonGenObject> objectList);

    /**
     * Called to process json data entry for meta data
     *
     * @param block      - block that is being process, keep in mind
     * @param element    - data
     * @param data       - if the process was called on a meta entry this will contain the entry, Keep in
     *                   mind it can be null for block only data and is not registered to the block
     * @param objectList - list to add generated objects
     */
    void process(MetaData data, BlockBase block, JsonElement element, List<IJsonGenObject> objectList);
}
