package com.builtbroken.mc.seven.framework.block.json;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.meta.MetaData;
import com.builtbroken.mc.seven.framework.block.tile.ITileProvider;
import com.builtbroken.mc.seven.framework.block.tile.TileProviderByClass;
import com.builtbroken.mc.seven.framework.block.tile.TileProviderMeta;
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
        return process(element, "");
    }

    public ITileProvider process(JsonElement element, String pack)
    {
        JsonObject data = element.getAsJsonObject();
        JsonProcessor.ensureValuesExist(data, "id", "class");

        String className = data.get("class").getAsString();
        if (!className.contains(".") || className.startsWith("."))
        {
            className = pack + className;
        }
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
        String pack = block.getClass().getName().replace("class", "");
        pack = pack.substring(0, pack.lastIndexOf("."));
        if (block.data.tileEntityProvider instanceof TileProviderMeta)
        {
            ((TileProviderMeta) block.data.tileEntityProvider).backupProvider = process(element, pack);
        }
        else
        {
            block.data.tileEntityProvider = process(element, pack);
        }
    }

    @Override
    public void process(MetaData meta, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        String pack = block.getClass().getName().replace("class", "");
        pack = pack.substring(0, pack.lastIndexOf("."));
        meta.tileEntityProvider = process(element, pack);
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
