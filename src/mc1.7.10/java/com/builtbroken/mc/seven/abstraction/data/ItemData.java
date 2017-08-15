package com.builtbroken.mc.seven.abstraction.data;

import com.builtbroken.mc.api.abstraction.data.IItemData;
import net.minecraft.item.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/15/2017.
 */
public class ItemData implements IItemData
{
    public final Item item;

    public ItemData(Item item)
    {
        this.item = item;
    }

    @Override
    public Item unwrap()
    {
        return item;
    }
}
