package com.builtbroken.mc.prefab.json.block.processor;

import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import com.builtbroken.mc.prefab.json.block.BlockJson;
import com.builtbroken.mc.prefab.json.block.meta.MetaData;
import com.builtbroken.mc.prefab.json.recipe.smelting.SmeltingRecipe;
import com.builtbroken.mc.prefab.json.recipe.smelting.SmeltingRecipeJson;
import com.builtbroken.mc.prefab.json.recipe.smelting.SmeltingRecipeText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a json entry into a smelting recipe
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class JsonBlockSmeltingProcessor extends JsonBlockSubProcessor implements ILoadable
{
    private List<SmeltingRecipe> recipes = new ArrayList();

    @Override
    public void process(BlockJson block, JsonElement element)
    {
        SmeltingRecipe recipe = process(block, element.getAsJsonObject());
        if (recipe != null)
        {
            recipes.add(recipe);
        }
    }

    @Override
    public void processMeta(MetaData meta, BlockJson block, JsonElement element)
    {
        SmeltingRecipe recipe = process(block, element.getAsJsonObject());
        if (recipe != null)
        {
            recipe.inputMeta = meta.index;
            recipes.add(recipe);
        }
    }

    private SmeltingRecipe process(BlockJson block, JsonObject element)
    {
        SmeltingRecipe recipe;
        if (element.has("output"))
        {
            JsonElement output = element.get("output");
            if (output.isJsonObject())
            {
                recipe = new SmeltingRecipeJson(block, output.getAsJsonObject());
            }
            else
            {
                recipe = new SmeltingRecipeText(block, output.getAsString());
            }
            if (element.has("xp"))
            {
                recipe.xp = (float) element.get("xp").getAsDouble();
            }
        }
        return null;
    }

    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {

    }

    @Override
    public void postInit()
    {
        recipes.forEach(SmeltingRecipe::register);
        recipes.clear(); //Clear cache to save that maybe 70kb of ram
    }
}
