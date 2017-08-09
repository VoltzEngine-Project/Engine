package com.builtbroken.mc.framework.json.processors.extra;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonOreNameData extends JsonGenData implements IPostInit
{
    public String name;
    public Object item;

    public JsonOreNameData(IJsonProcessor processor, String name, Object item)
    {
        super(processor);
        this.name = name;
        this.item = item;
    }

    @Override
    public void onPostInit()
    {
        ItemStack stack = toStack(item);
        if (stack != null)
        {
            OreDictionary.registerOre(name, stack);
        }
        else
        {
            Engine.error("JsonOreNameData: Failed to parse item, this will prevent the ore name '" + name + "' from being registered to " + item);
        }
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
