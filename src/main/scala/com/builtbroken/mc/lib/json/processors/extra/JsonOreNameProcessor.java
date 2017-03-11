package com.builtbroken.mc.lib.json.processors.extra;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.builtbroken.mc.lib.json.processors.block.BlockJson;
import com.builtbroken.mc.lib.json.processors.block.meta.MetaData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

/**
 * Used to append orenames to exist blocks and items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonOreNameProcessor extends JsonProcessor<JsonOreNameData> implements IJsonBlockSubProcessor
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "oreName";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:item";
    }

    @Override
    public JsonOreNameData process(JsonElement element)
    {
        JsonObject data = element.getAsJsonObject();
        ensureValuesExist(data, "name", "item");
        String name = data.getAsJsonPrimitive("name").getAsString();
        String item = data.getAsJsonPrimitive("item").getAsString();
        return new JsonOreNameData(name, item);
    }

    @Override
    public void process(BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        if (element instanceof JsonObject)
        {
            JsonObject data = element.getAsJsonObject();
            ensureValuesExist(data, "name");
            block.oreName = data.getAsJsonPrimitive("name").getAsString();
        }
        else if (element instanceof JsonPrimitive)
        {
            block.oreName = element.getAsJsonPrimitive().getAsString();
        }
    }

    @Override
    public void process(MetaData meta, BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        if (element instanceof JsonObject)
        {
            JsonObject data = element.getAsJsonObject();
            ensureValuesExist(data, "name");
            meta.addOreName(data.getAsJsonPrimitive("name").getAsString());
        }
        else if (element instanceof JsonPrimitive)
        {
            meta.addOreName(element.getAsJsonPrimitive().getAsString());
        }
    }
}
