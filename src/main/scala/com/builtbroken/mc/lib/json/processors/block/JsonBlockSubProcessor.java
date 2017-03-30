package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * Used to process sub entries in a block file. Useful for handling recipes, registry calls, etc...
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class JsonBlockSubProcessor implements IJsonBlockSubProcessor
{
    @Override
    public boolean canProcess(String key, JsonElement element)
    {
        return true;
    }

    @Override
    public void process(BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {

    }

    @Override
    public void process(MetaData data, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {

    }
}
