package com.builtbroken.mc.lib.json.processors.item;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * Loads basic item data from a processor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonItemProcessor extends JsonProcessor<ItemJson>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "item";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:block";
    }

    @Override
    public ItemJson process(JsonElement element)
    {
        return null;
    }
}
