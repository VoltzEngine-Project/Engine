package com.builtbroken.mc.framework.item.wrapper;

import com.builtbroken.mc.framework.item.ItemBase;
import com.builtbroken.mc.framework.item.logic.ItemNode;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2017.
 */
public class ItemWrapper extends ItemBase
{
    public final ItemNode node;

    public ItemWrapper(ItemNode node)
    {
        super(node.mod, node.id);
        this.node = node;
    }

    @Override
    public String getRenderContentID(int meta)
    {
        return null;
    }
}
