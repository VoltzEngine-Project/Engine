package com.builtbroken.mc.prefab.json.block.processor;

import com.builtbroken.mc.core.Engine;
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
        try
        {
            SmeltingRecipe recipe = process(block, element.getAsJsonObject());
            recipes.add(recipe);
        }
        catch (Exception e)
        {
            //TODO log errors in a list to display to users after MC is done loading
            Engine.logger().error("JsonBlockSmeltingProcessor: Failed to process recipe data for " + block, e);
        }
    }

    @Override
    public void processMeta(MetaData meta, BlockJson block, JsonElement element)
    {
        try
        {
            SmeltingRecipe recipe = process(block, element.getAsJsonObject());
            recipe.inputMeta = meta.index;
            recipes.add(recipe);
        }
        catch (Exception e)
        {
            //TODO log errors in a list to display to users after MC is done loading
            Engine.logger().error("JsonBlockSmeltingProcessor: Failed to process recipe data for " + block + " @ " + meta, e);
        }
    }

    /**
     * Called to process recipe data from the json element
     *
     * @param block
     * @param json  - data stored in a json object, must have an output key
     * @return smelting recipe
     * @throws IllegalArgumentException - if the input is invalid
     */
    public SmeltingRecipe process(BlockJson block, JsonObject json)
    {
        SmeltingRecipe recipe;
        if (json.has("output"))
        {
            JsonElement output = json.get("output");
            if (output.isJsonObject())
            {
                JsonObject json2 = output.getAsJsonObject();
                if (!json2.has("item"))
                {
                    throw new IllegalArgumentException(this + " json data is missing a item entry used to figure out what to load.");
                }
                if (!json2.has("type"))
                {
                    throw new IllegalArgumentException(this + " json data is missing a type entry used to figure out what to load.");
                }
                recipe = new SmeltingRecipeJson(block, json2);
            }
            else
            {
                recipe = new SmeltingRecipeText(block, output.getAsString());
            }
            if (json.has("xp"))
            {
                recipe.xp = (float) json.get("xp").getAsDouble();
            }
        }
        throw new IllegalArgumentException(this + " json data is missing a item entry used to figure out what to load.");
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
