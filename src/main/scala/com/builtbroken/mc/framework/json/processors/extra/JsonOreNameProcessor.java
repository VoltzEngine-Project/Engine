package com.builtbroken.mc.framework.json.processors.extra;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
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
    public static final String KEY = "oreName";
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
        return "after:item";
    }

    @Override
    public JsonOreNameData process(JsonElement element)
    {
        JsonObject data = element.getAsJsonObject();
        ensureValuesExist(data, "name", "item");
        String name = data.getAsJsonPrimitive("name").getAsString();
        String item = data.getAsJsonPrimitive("item").getAsString();
        return new JsonOreNameData(this, name, item);
    }

    @Override
    public void process(BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        if (element instanceof JsonObject)
        {
            JsonObject data = element.getAsJsonObject();
            ensureValuesExist(data, "name");
            block.data.oreName = data.getAsJsonPrimitive("name").getAsString();
        }
        else if (element instanceof JsonPrimitive)
        {
            block.data.oreName = element.getAsJsonPrimitive().getAsString();
        }
    }

    @Override
    public void process(MetaData meta, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
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
