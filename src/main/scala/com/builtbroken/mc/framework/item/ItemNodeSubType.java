package com.builtbroken.mc.framework.item;

import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.loading.JsonProcessorInjectionMap;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/19/2017.
 */
public class ItemNodeSubType
{
    /** Unique ID of the content */
    public final String id;
    /** Unlocalized name */
    public final String unlocalizedName;
    /** Metadata/subtype index */
    public final int index;

    public final ItemBase item;
    public final ItemNode node;

    public String oreName;

    //Injection handler for processing JSON
    private JsonProcessorInjectionMap<ItemNodeSubType> itemNodeSubTypeJsonProcessorInjectionMap = new JsonProcessorInjectionMap<>(ItemNodeSubType.class);

    public ItemNodeSubType(ItemBase item, ItemNode node, String id, String unlocalizedName, int index)
    {
        this.item = item;
        this.node = node;
        this.id = id;
        this.unlocalizedName = unlocalizedName;
        this.index = index;
    }

    public ItemNodeSubType(ItemBase item, ItemNode node, JsonObject itemData)
    {
        this.item = item;
        this.node = node;

        //Ensure minimal values exist
        JsonProcessor.ensureValuesExist(itemData, "id", "name", "index");

        //Load required values
        this.id = itemData.getAsJsonPrimitive("id").getAsString().toLowerCase();
        this.unlocalizedName = itemData.getAsJsonPrimitive("name").getAsString();
        this.index = itemData.getAsJsonPrimitive("index").getAsInt();

        //Validate data
        if (index < 0 || index >= 32000)
        {
            throw new IllegalArgumentException("ItemNodeSubType#new() >> Meta index must be between 0 and 31999");
        }

        //Handle data injection
        for (Map.Entry<String, JsonElement> elementEntry : itemData.entrySet())
        {
            //TODO add debug
            itemNodeSubTypeJsonProcessorInjectionMap.handle(this, elementEntry.getKey(), elementEntry.getValue());
        }
    }

    @JsonProcessorData("ore") //TODO add array version
    public void setOreName(String value)
    {
        oreName = value;
    }

    @Override
    public String toString()
    {
        return "ItemNodeSubType[i:" + id + " m:" + index + " u: " + unlocalizedName + "]@" + hashCode();
    }
}
