package com.builtbroken.mc.framework.item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/19/2017.
 */
public class ItemNodeSubType
{
    /** Unique ID of the content */
    public final String id;
    /** Unlocalized name */
    public final String name;
    /** Metadata/subtype index */
    public final int index;

    public ItemNodeSubType(String id, String name, int index)
    {
        this.id = id;
        this.name = name;
        this.index = index;
    }
}
