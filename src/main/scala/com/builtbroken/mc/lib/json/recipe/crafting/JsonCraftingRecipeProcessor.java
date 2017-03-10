package com.builtbroken.mc.lib.json.recipe.crafting;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.block.BlockJson;
import com.builtbroken.mc.lib.json.block.meta.MetaData;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to load recipes from json into the game.
 * <p>
 * Recipes are loaded as data and then turned into recipes in the post init phase. This should
 * allow all mods to register items and ore dictionary values.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class JsonCraftingRecipeProcessor extends JsonProcessor<JsonCraftingRecipeData> implements IJsonBlockSubProcessor
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "craftingGridRecipe";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:item";
    }

    @Override
    public void process(JsonElement element, List<IJsonGenObject> objects)
    {
        JsonCraftingRecipeData data = process(null, element);
        if (data != null)
        {
            objects.add(data);
        }
    }

    /**
     * Called to process a recipe
     *
     * @param out     - optional, output item - if provided will not require output from recipe json
     * @param element - data containing the recipe
     * @return recipe data
     */
    public JsonCraftingRecipeData process(final Object out, final JsonElement element)
    {
        final JsonObject recipeData = element.getAsJsonObject();

        ensureValuesExist(recipeData, "type");
        Object output = out;
        if (output == null)
        {
            ensureValuesExist(recipeData, "output");
            output = recipeData.getAsJsonPrimitive("output").getAsString();
        }

        String type = recipeData.getAsJsonPrimitive("type").getAsString();
        if (type.equalsIgnoreCase("shaped"))
        {
            ensureValuesExist(recipeData, "grid", "items");
            //Load grid as string, and split to get rows
            String[] grid = recipeData.getAsJsonPrimitive("grid").getAsString().split(",");

            //Load items as object
            JsonObject itemObject = recipeData.getAsJsonObject("items");

            //Loop elements of object to get each item entry
            HashMap<Character, String> items = new HashMap();
            for (Map.Entry<String, JsonElement> entry : itemObject.entrySet())
            {
                if (entry.getKey().length() == 1)
                {
                    items.put(entry.getKey().charAt(0), entry.getValue().getAsJsonPrimitive().getAsString());
                }
                else
                {
                    throw new IllegalArgumentException("File contains invalid recipe data for item entry in recipe [" + entry.getKey() + " -> " + entry.getValue() + "] each item must be represented by a single character.");
                }
            }
            //Convert everything to data for recipe
            Object[] data = new Object[grid.length + items.size() * 2];
            int i;
            for (i = 0; i < grid.length; i++)
            {
                data[i] = grid[i];
            }
            for (Map.Entry<Character, String> entry : items.entrySet())
            {
                data[i++] = entry.getKey();
                data[i++] = entry.getValue();
            }
            //New recipe data
            return new JsonCraftingRecipeData(output, data, true);
        }
        else if (type.equalsIgnoreCase("shapeless"))
        {
            ensureValuesExist(recipeData, "items");

            //Load items
            String[] items = recipeData.getAsJsonPrimitive("items").getAsString().split(",");

            //New recipe data
            return new JsonCraftingRecipeData(output, items, false);
        }
        else
        {
            throw new IllegalArgumentException("File is contains an unknown grid recipe type of " + type + " that is not supported.");
        }
    }

    @Override
    public void process(BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        process(block, element);
    }

    @Override
    public void process(MetaData data, BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        process(new ItemStack(block, 1, data.index), element);
    }
}
