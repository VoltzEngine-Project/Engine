package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.framework.block.tile.TileProviderByClass;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Used to append orenames to exist blocks and items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonBlockTileProcessor extends JsonBlockSubProcessor
{
    public static final String KEY = "tileEntity";

    @Override
    public void process(BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        JsonObject data = element.getAsJsonObject();
        JsonProcessor.ensureValuesExist(data, "id", "class");
        try
        {
            block.data.tileEntityProvider = new TileProviderByClass(data.get("id").getAsString(), data.get("class").getAsString());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to find class for " + data.get("class").getAsString() + " while parsing json tile data");
        }
    }

    @Override
    public void process(MetaData meta, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        JsonObject data = element.getAsJsonObject();
        JsonProcessor.ensureValuesExist(data, "id", "class");
        try
        {
            meta.tileEntityProvider = new TileProviderByClass(data.get("id").getAsString(), data.get("class").getAsString());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to find class for " + data.get("class").getAsString() + " while parsing json tile data");
        }
    }
}
