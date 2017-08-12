package com.builtbroken.mc.seven.framework.json.recipe.crafting;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.exceptions.JsonFormatException;
import com.builtbroken.mc.seven.framework.block.IJsonBlockSubProcessor;
import com.builtbroken.mc.seven.framework.json.extra.JsonOreNameProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.JsonRecipeProcessor;
import com.google.gson.JsonArray;
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
    public JsonCraftingRecipeData process(final Object out, final JsonElement element) throws JsonFormatException
    {
        final JsonObject recipeData = element.getAsJsonObject();

        //Ensure mandatory objects exist
        ensureValuesExist(recipeData, "type");

        //Get output if missing
        Object output = out;
        if (output == null)
        {
            ensureValuesExist(recipeData, "output");
            JsonElement outputElement = recipeData.get("output");
            output = getItemFromJson(outputElement);
        }

        //Get type and process based on type
        final String recipeType = recipeData.getAsJsonPrimitive("type").getAsString();
        if (recipeType.equalsIgnoreCase("shaped"))
        {
            //Ensure data exists
            ensureValuesExist(recipeData, "grid", "items");

            //Load grid
            String[] craftingGridRows;
            JsonElement gridElement = recipeData.get("grid");
            if (gridElement.isJsonPrimitive())
            {
                //Load grid as string, and split to get rows
                craftingGridRows = gridElement.getAsJsonPrimitive().getAsString().split(",");
            }
            else
            {
                JsonArray gridArray = gridElement.getAsJsonArray();
                craftingGridRows = new String[gridArray.size()];
                int i = 0;
                for (JsonElement e : gridArray)
                {
                    if (e.isJsonPrimitive())
                    {
                        craftingGridRows[i++] = e.getAsJsonPrimitive().getAsString();
                    }
                    else
                    {
                        throw new JsonFormatException("Recipe array must contain only string values of characters representing items.");
                    }
                }
            }

            //Load items as object
            JsonObject itemObject = recipeData.getAsJsonObject("items");

            //Loop elements of object to get each item entry
            HashMap<Character, Object> items = new HashMap();
            for (Map.Entry<String, JsonElement> entry : itemObject.entrySet())
            {
                if (entry.getKey().length() == 1)
                {
                    char c = entry.getKey().charAt(0);
                    if (c == '.' || Character.isWhitespace(c))
                    {
                        throw new JsonFormatException("File contains invalid recipe data for item entry in recipe [" + entry.getKey() + " -> " + entry.getValue() + "]. Each entry must be a single character that is not a space or a '.' which is used in place of a space.");

                    }
                    items.put(c, getItemFromJson(entry.getValue()));
                }
                else
                {
                    throw new JsonFormatException("File contains invalid recipe data for item entry in recipe [" + entry.getKey() + " -> " + entry.getValue() + "] each item must be represented by a single character.");
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
                    throw new JsonFormatException("Crafting grid row[" + i + "] is not the same size of " + size + " as previous grid rows. This will prevent the recipe from loading correctly and needs to be fixed.");
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
                            throw new JsonFormatException("File is missing an entry for item linked to '" + c + "' for crafting grid row[" + i + "] index[" + charIndex + "] in recipe data -> " + recipeData);
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
            for (Map.Entry<Character, Object> entry : items.entrySet())
            {
                data[i++] = entry.getKey();
                data[i++] = entry.getValue();
            }

            //New recipe data
            return new JsonCraftingRecipeData(this, output, data, true, largeGrid);
        }
        else if (recipeType.equalsIgnoreCase("shapeless"))
        {
            ensureValuesExist(recipeData, "items");

            //Load items
            String[] items = recipeData.getAsJsonPrimitive("items").getAsString().split(",");

            //New recipe data
            return new JsonCraftingRecipeData(this, output, items, false, items.length > 9);
        }
        else if (recipeType.equalsIgnoreCase("sheetmetal")) //TODO make into reg object
        {
            return null; //TODO implement
        }
        else if (recipeType.equalsIgnoreCase("tools")) //TODO make into reg object
        {
            return null;//TODO implement
        }
        else
        {
            throw new JsonFormatException("File is contains an unknown grid recipe type of " + recipeType + " that is not supported.");
        }
    }
}
