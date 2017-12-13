package com.builtbroken.mc.framework.json.conversion.data.mc;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.data.JsonBlockEntry;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonConverterBlock extends JsonConverter<JsonBlockEntry>
{
    public JsonConverterBlock()
    {
        super("block");
    }

    @Override
    public JsonBlockEntry convert(JsonElement element, String... args)
    {
        if(element.isJsonPrimitive())
        {
            return new JsonBlockEntry(element.getAsString());
        }
        throw new IllegalArgumentException("JsonConverterBlock: could not convert json to block reference, json: " + element);
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if(data instanceof JsonBlockEntry)
        {
            return new JsonPrimitive(((JsonBlockEntry) data).getRegistryName());
        }
        else if(data instanceof Block)
        {
            return new JsonPrimitive(InventoryUtility.getRegistryName((Block)data));
        }
        return null;
    }
}
