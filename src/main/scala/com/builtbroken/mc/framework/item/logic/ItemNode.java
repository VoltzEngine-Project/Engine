package com.builtbroken.mc.framework.item.logic;

/**
 * Node to handle logic for an item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2017.
 */
public class ItemNode
{
    /** Domain of the mod that owners this content */
    public final String owner;
    /** Unique ID of the content */
    public final String id;

    public ItemNode(String owner, String id)
    {
        this.owner = owner;
        this.id = id;
    }
}
