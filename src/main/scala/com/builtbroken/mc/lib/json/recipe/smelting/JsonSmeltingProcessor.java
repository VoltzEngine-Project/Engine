package com.builtbroken.mc.lib.json.recipe.smelting;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * Loads smelting recipes from a json
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonSmeltingProcessor extends JsonProcessor<JsonSmeltingRecipeData>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "smeltingRecipe";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:item";
    }

    @Override
    public JsonSmeltingRecipeData process(JsonElement element)
    {
        return null;
    }
}
