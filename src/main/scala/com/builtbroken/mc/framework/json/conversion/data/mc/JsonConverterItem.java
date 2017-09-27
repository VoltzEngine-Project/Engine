package com.builtbroken.mc.framework.json.conversion.data.mc;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.data.JsonItemEntry;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Used to convert json data to pos objects
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2017.
 */
public class JsonConverterItem extends JsonConverter<JsonItemEntry>
{
    public JsonConverterItem()
    {
        super("item");
    }

    @Override
    public JsonItemEntry convert(JsonElement element, String... args)
    {
        return fromJson(element.getAsJsonObject());
    }

    public static JsonItemEntry fromJson(JsonObject itemStackObject)
    {
        //Convert and check types
        JsonProcessor.ensureValuesExist(itemStackObject, "item");

        //Create entry
        JsonItemEntry entry = new JsonItemEntry();

        //Get required data
        entry.item = itemStackObject.get("item").getAsString();
        if (itemStackObject.has("meta"))
        {
            entry.damage = itemStackObject.get("meta").getAsString();
        }
        else if (itemStackObject.has("damage"))
        {
            entry.damage = itemStackObject.get("damage").getAsString();
        }

        //Load optional stacksize
        if (itemStackObject.has("count"))
        {
            entry.count = itemStackObject.getAsJsonPrimitive("count").getAsInt();
            if (entry.count < 0)
            {
                throw new RuntimeException("Recipe output count must be above zero");
            }
            else if (entry.count > 64)
            {
                throw new RuntimeException("Recipe output count must be below 64 as this is the max stacksize for this version of Minecraft.");
            }
        }

        //Load optional item data
        if (itemStackObject.has("nbt"))
        {
            entry.nbt = JsonConverterNBT.handle(itemStackObject.get("nbt"));
            if (entry.nbt.hasNoTags())
            {
                entry.nbt = null;
            }
        }
        return entry;
    }
}
