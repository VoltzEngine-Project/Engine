package com.builtbroken.mc.core.commands.json;

import com.builtbroken.jlib.lang.EnglishLetters;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.json.processors.recipe.crafting.JsonCraftingRecipeData;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandJsonRecipe extends SubCommand
{
    public CommandJsonRecipe()
    {
        super("recipe");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handleConsoleCommand(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equals("generate") || args[0].equals("gen"))
            {
                if (args.length > 1)
                {
                    String entryID = args[1];
                    ItemStack stack = new JsonCraftingRecipeData(null, null, null, false, false).toStack(entryID);
                    if (stack != null)
                    {
                        List<IRecipe> recipes = InventoryUtility.getRecipesWithOutput(stack);
                        if (recipes != null)
                        {
                            sender.addChatMessage(new ChatComponentText("Found " + recipes.size() + " for '" + entryID + "' saving to external json file"));
                            File writeFile = new File(JsonContentLoader.INSTANCE.externalContentFolder, (entryID + "-recipes.json").replace(":", "_"));
                            try
                            {
                                JsonObject object = new JsonObject();
                                int index = 0;
                                for (IRecipe recipe : recipes)
                                {
                                    if (recipe instanceof ShapedOreRecipe)
                                    {
                                        JsonObject recipeObject = new JsonObject();
                                        recipeObject.add("type", new JsonPrimitive("shaped"));
                                        recipeObject.add("output", new JsonPrimitive(toString(recipe.getRecipeOutput())));
                                        recipeObject.add("grid", new JsonPrimitive(""));

                                        object.add("craftingGridRecipe:" + (index++), recipeObject);
                                    }
                                    else if (recipe instanceof ShapedRecipes)
                                    {
                                        Pair<String, HashMap<String, String>> itemSet = generateItemData(((ShapedRecipes) recipe).recipeItems, ((ShapedRecipes) recipe).recipeWidth);

                                        //Build data
                                        if (itemSet != null)
                                        {
                                            JsonObject recipeObject = new JsonObject();
                                            recipeObject.add("type", new JsonPrimitive("shaped"));
                                            recipeObject.add("output", new JsonPrimitive(toString(recipe.getRecipeOutput())));
                                            recipeObject.add("grid", new JsonPrimitive(itemSet.left()));

                                            JsonArray array = new JsonArray();
                                            for (Map.Entry<String, String> entry : itemSet.right().entrySet())
                                            {
                                                JsonObject itemEntry = new JsonObject();
                                                itemEntry.add(entry.getValue(), new JsonPrimitive(entry.getKey()));
                                                array.add(itemEntry);
                                            }
                                            recipeObject.add("items", array);

                                            object.add("craftingGridRecipe:" + (index++), recipeObject);
                                        }
                                        else
                                        {
                                            sender.addChatMessage(new ChatComponentText("Failed to map recipe items for '" + recipe + "'"));
                                        }
                                    }
                                    else
                                    {
                                        sender.addChatMessage(new ChatComponentText("Failed to ID recipe type of '" + recipe + "'"));
                                    }
                                }

                                if (object.entrySet().size() > 0)
                                {
                                    try (FileWriter file = new FileWriter(writeFile))
                                    {
                                        file.write(object.toString());
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("Failed to locate recipes for '" + entryID + "'"));
                        }
                    }
                    else
                    {
                        sender.addChatMessage(new ChatComponentText("Failed to locate entry for '" + entryID + "'"));
                    }
                    return true;
                }
            }
        }
        return handleHelp(sender, args);
    }

    protected Pair<String, HashMap<String, String>> generateItemData(ItemStack[] recipeItems, int width)
    {
        HashMap<String, String> items = new HashMap();
        String grid = "";

        int c = 0;
        int w = 0;

        //Map items to characters
        boolean upper = false;
        if (recipeItems != null)
        {
            for (ItemStack st : recipeItems)
            {
                if (c > EnglishLetters.values().length)
                {
                    c = 0;
                    upper = true;
                }
                else
                {
                    return null; //too many chars in use to be valid
                }
                if (c < EnglishLetters.values().length)
                {
                    String ch = EnglishLetters.values()[c].name();
                    items.put(upper ? ch : ch.toLowerCase(), toString(st));

                    grid += ch;
                    if (w++ >= width)
                    {
                        w = 0;
                        grid += ",";
                    }
                }
                c++;
            }
            return new Pair(grid, items);
        }
        return null;
    }

    protected String toString(Object output)
    {
        return "null";
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("generate mod:itemName - generates json recipes using the items existing recipes");
        items.add("generate item@mod:itemName - forces to use item list");
        items.add("generate block@mod:itemName - forces to use block list");
    }
}
