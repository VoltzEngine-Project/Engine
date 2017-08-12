package com.builtbroken.mc.seven.framework.json;

import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockProcessor;
import com.builtbroken.mc.seven.framework.json.extra.JsonOreNameProcessor;
import com.builtbroken.mc.seven.framework.json.item.JsonItemProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.crafting.JsonCraftingRecipeProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.replace.JsonRecipeReplacementProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.smelting.JsonFurnaceRecipeProcessor;
import com.builtbroken.mc.seven.framework.json.world.JsonWorldOreGenProcessor;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class JsonModule extends AbstractLoadable
{
    @Override
    public void preInit()
    {
        JsonContentLoader.INSTANCE.add(new JsonBlockProcessor());
        JsonContentLoader.INSTANCE.add(new JsonItemProcessor());
        //TODO load entities


        JsonContentLoader.INSTANCE.add(new JsonOreNameProcessor());
        JsonContentLoader.INSTANCE.add(new JsonWorldOreGenProcessor());

        JsonContentLoader.INSTANCE.add(new JsonCraftingRecipeProcessor());
        JsonContentLoader.INSTANCE.add(new JsonFurnaceRecipeProcessor());
        JsonContentLoader.INSTANCE.add(new JsonRecipeReplacementProcessor());
    }
}
