package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.framework.block.tile.ITileProvider;
import com.builtbroken.mc.framework.block.tile.TileProviderByClass;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
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
public class JsonBlockTileProcessor extends JsonProcessor<ITileProvider> implements IJsonBlockSubProcessor
{
    public static final String KEY = "tileEntity";

    @Override
    public ITileProvider process(JsonElement element)
    {
        JsonObject data = element.getAsJsonObject();
        JsonProcessor.ensureValuesExist(data, "id", "class");

        String className = data.get("class").getAsString();
        String id = data.get("id").getAsString();
        try
        {
            return new TileProviderByClass(this, id, className);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to find class for '" + className + "' while parsing json tile data");
        }
    }

    @Override
    public void process(BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        block.data.tileEntityProvider = process(element);
    }

    @Override
    public void process(MetaData meta, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        meta.tileEntityProvider = process(element);
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
}
