package com.builtbroken.mc.lib.json.processors.world;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.builtbroken.mc.lib.json.processors.block.BlockJson;
import com.builtbroken.mc.lib.json.processors.block.meta.MetaData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public class JsonWorldOreGenProcessor extends JsonProcessor<JsonWorldOreGenData> implements IJsonBlockSubProcessor
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "worldGenerator";
    }

    @Override
    public String getLoadOrder()
    {
        return "block";
    }

    @Override
    public JsonWorldOreGenData process(JsonElement element)
    {
        return process(null, -1, null, element);
    }

    public JsonWorldOreGenData process(Block block, int meta, String ore, JsonElement element)
    {
        JsonObject genData = element.getAsJsonObject();
        ensureValuesExist(genData, "type", "minY", "maxY", "branchSize", "chunkLimit");

        //Get ore block
        Object oreBlock;
        if (block == null)
        {
            ensureValuesExist(genData, "block");
            oreBlock = genData.getAsJsonPrimitive("block").getAsString();
        }
        else
        {
            oreBlock = new ItemStack(block, 1, meta >= 0 ? meta : 0);
        }
        String oreName = ore;
        if (oreName == null)
        {
            ensureValuesExist(genData, "oreName");
            oreName = genData.getAsJsonPrimitive("oreName").getAsString();
        }
        String type = genData.getAsJsonPrimitive("type").getAsString();
        int min = genData.getAsJsonPrimitive("minY").getAsInt();
        int max = genData.getAsJsonPrimitive("maxY").getAsInt();
        int branch = genData.getAsJsonPrimitive("branchSize").getAsInt();
        int chunk = genData.getAsJsonPrimitive("chunkLimit").getAsInt();

        if (min > 255 || min < 0 || max > 255 || max < 0 || max < min)
        {
            throw new RuntimeException("Min & Max value for ore generators must be between 0-255, and max must be equal to or greater than min");
        }

        if (branch <= 0 || chunk <= 0)
        {
            throw new RuntimeException("Branch size per generation and chunk generation limit must be greater than zero.");
        }

        if (type.equalsIgnoreCase("StoneOre"))
        {
            return new JsonWorldOreGenData(this, oreBlock, oreName, min, max, branch, chunk);
        }
        else
        {
            throw new RuntimeException("Failed to ID world ore gen type of " + type);
        }
    }

    @Override
    public void process(BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        JsonWorldOreGenData object = process(block, 0, block.oreName, element);
        if (object != null)
        {
            objectList.add(object);
        }
    }

    @Override
    public void process(MetaData data, BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        JsonWorldOreGenData object = process(block, data.index, data.oreNames != null && data.oreNames.size() > 0 ? data.oreNames.get(0) : null, element);
        if (object != null)
        {
            objectList.add(object);
        }
    }
}
