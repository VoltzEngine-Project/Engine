package com.builtbroken.mc.lib.json.block.processor;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.block.BlockJson;
import com.builtbroken.mc.lib.json.block.meta.MetaData;
import com.builtbroken.mc.lib.json.recipe.smelting.JsonSmeltingRecipeData;
import com.builtbroken.mc.lib.mod.loadable.ILoadable;
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
    private List<JsonSmeltingRecipeData> recipes = new ArrayList();

    @Override
    public void process(BlockJson block, JsonElement element)
    {
        try
        {
            JsonSmeltingRecipeData recipe = process(block, element.getAsJsonObject());
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
            JsonSmeltingRecipeData recipe = process(block, element.getAsJsonObject());
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
    public JsonSmeltingRecipeData process(BlockJson block, JsonObject json)
    {
        return null; //TODO implement
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
        recipes.forEach(JsonSmeltingRecipeData::register);
        recipes.clear(); //Clear cache to save that maybe 70kb of ram
    }
}
