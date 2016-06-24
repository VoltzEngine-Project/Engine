package com.builtbroken.mc.prefab.json.processors;

import com.builtbroken.mc.prefab.json.BlockJson;
import com.builtbroken.mc.prefab.json.BlockJsonMeta;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Load generic block data from a json
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonBlockProcessor extends JsonProcessor<BlockJson>
{
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
}
