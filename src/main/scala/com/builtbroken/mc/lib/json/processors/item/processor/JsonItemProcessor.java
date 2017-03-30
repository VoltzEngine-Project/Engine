package com.builtbroken.mc.lib.json.processors.item.processor;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.builtbroken.mc.lib.json.processors.block.JsonBlockProcessor;
import com.builtbroken.mc.lib.json.processors.item.ItemJson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Loads basic item data from a processor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonItemProcessor extends JsonProcessor<ItemJson>
{
    public static final String KEY = "item";

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
        return "after:" + JsonBlockProcessor.KEY;
    }

    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> objectList)
    {
        JsonObject itemData = element.getAsJsonObject();
        ensureValuesExist(itemData, "name", "id", "mod");
        ItemJson item = new ItemJson(itemData.getAsJsonPrimitive("id").getAsString(), itemData.getAsJsonPrimitive("mod").getAsString(), itemData.getAsJsonPrimitive("name").getAsString());

        //TODO implement subtypes and other data

        objectList.add(item);
        return true;
    }
}
