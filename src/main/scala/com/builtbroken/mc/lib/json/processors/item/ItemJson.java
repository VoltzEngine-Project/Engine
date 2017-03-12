package com.builtbroken.mc.lib.json.processors.item;

import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import net.minecraft.item.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class ItemJson extends Item implements IJsonGenObject
{
    @Override
    public void register()
    {

    }

    @Override
    public String getLoader()
    {
        return "item";
    }
}
