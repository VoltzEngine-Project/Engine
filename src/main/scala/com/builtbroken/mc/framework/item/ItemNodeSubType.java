package com.builtbroken.mc.framework.item;

import com.builtbroken.mc.framework.json.loading.JsonProcessorData;

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

    public ItemNodeSubType(ItemBase item, ItemNode node, String id, String unlocalizedName, int index)
    {
        this.item = item;
        this.node = node;
        this.id = id;
        this.unlocalizedName = unlocalizedName;
        this.index = index;
    }

    @JsonProcessorData("ore") //TODO add array version
    public void addOreDictValue(String value)
    {
        oreName = value;
    }
}
