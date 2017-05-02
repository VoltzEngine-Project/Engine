package com.builtbroken.mc.lib.json.processors.recipe.crafting;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.processors.extra.JsonOreNameProcessor;
import com.builtbroken.mc.lib.json.processors.recipe.JsonRecipeProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
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
public class JsonCraftingRecipeProcessor extends JsonRecipeProcessor<JsonCraftingRecipeData> implements IJsonBlockSubProcessor
{
    public static final String KEY = "craftingGridRecipe";

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return KEY;
    }

    @Override
    public String getLoadOrder()
    {
        return "after:" + JsonOreNameProcessor.KEY;
    }

    @Override
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
            String[] craftingGridRows = recipeData.getAsJsonPrimitive("grid").getAsString().split(",");

            //Load items as object
            JsonObject itemObject = recipeData.getAsJsonObject("items");

            //Loop elements of object to get each item entry
            HashMap<Character, String> items = new HashMap();
            for (Map.Entry<String, JsonElement> entry : itemObject.entrySet())
            {
                if (entry.getKey().length() == 1)
                {
                    char c = entry.getKey().charAt(0);
                    if (c == '.' || Character.isWhitespace(c))
                    {
                        throw new IllegalArgumentException("File contains invalid recipe data for item entry in recipe [" + entry.getKey() + " -> " + entry.getValue() + "]. Each entry must be a single character that is not a space or a '.' which is used in place of a space.");

                    }
                    items.put(c, entry.getValue().getAsJsonPrimitive().getAsString());
                }
                else
                {
                    throw new IllegalArgumentException("File contains invalid recipe data for item entry in recipe [" + entry.getKey() + " -> " + entry.getValue() + "] each item must be represented by a single character.");
                }
            }

            //validate grid layout and components
            boolean largeGrid = craftingGridRows.length > 3;
            int size = 0;
            for (int i = 0; i < craftingGridRows.length; i++)
            {
                String gridRow = craftingGridRows[i];
                //Replace spacer
                if (gridRow.contains("."))
                {
                    gridRow = gridRow.replace('.', ' ');
                    craftingGridRows[i] = gridRow;
                }

                int l = gridRow.length();

                //Invalid recipe
                if (size > 0 && l != size)
                {
                    throw new IllegalArgumentException("Crafting grid row[" + i + "] is not the same size of " + size + " as previous grid rows. This will prevent the recipe from loading correctly and needs to be fixed.");
                }
                //Increase size smaller, use for validation
                if (l > size)
                {
                    size = l;
                }
                //if larger than 3 the grid needs to use a different recipe
                if (l > 3)
                {
                    largeGrid = true;
                }
                //Ensure all components exist
                final char[] chars = gridRow.toCharArray();
                for (int charIndex = 0; charIndex < chars.length; charIndex++)
                {
                    final char c = chars[charIndex];
                    //Ignore whitespace
                    if (!Character.isWhitespace(c))
                    {
                        if (!items.containsKey(c))
                        {
                            throw new IllegalArgumentException("File is missing an entry for item linked to '" + c + "' for crafting grid row[" + i + "] index[" + charIndex + "] in recipe data -> " + recipeData);
                        }
                    }
                }
            }


            //Convert everything to data for recipe
            Object[] data = new Object[craftingGridRows.length + items.size() * 2];
            int i;
            for (i = 0; i < craftingGridRows.length; i++)
            {
                data[i] = craftingGridRows[i];
            }
            for (Map.Entry<Character, String> entry : items.entrySet())
            {
                data[i++] = entry.getKey();
                data[i++] = entry.getValue();
            }

            //New recipe data
            return new JsonCraftingRecipeData(this, output, data, true, largeGrid);
        }
        else if (type.equalsIgnoreCase("shapeless"))
        {
            ensureValuesExist(recipeData, "items");

            //Load items
            String[] items = recipeData.getAsJsonPrimitive("items").getAsString().split(",");

            //New recipe data
            return new JsonCraftingRecipeData(this, output, items, false, items.length > 9);
        }
        else if (type.equalsIgnoreCase("sheetmetal")) //TODO make into reg object
        {
            return null; //TODO implement
        }
        else if (type.equalsIgnoreCase("tools")) //TODO make into reg object
        {
            return null;//TODO implement
        }
        else
        {
            throw new IllegalArgumentException("File is contains an unknown grid recipe type of " + type + " that is not supported.");
        }
    }
}
