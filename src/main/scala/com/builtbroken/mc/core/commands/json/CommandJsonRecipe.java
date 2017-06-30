package com.builtbroken.mc.core.commands.json;

import com.builtbroken.jlib.lang.EnglishLetters;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.json.conversion.JsonConverterNBT;
import com.builtbroken.mc.lib.json.processors.recipe.crafting.JsonCraftingRecipeData;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
                        List<IRecipe> recipes = entryID.contains("#") ? InventoryUtility.getRecipesWithOutput(stack) : InventoryUtility.getRecipesWithOutput(stack.getItem());
                        if (recipes != null)
                        {
                            sender.addChatMessage(new ChatComponentText("Found " + recipes.size() + " for '" + entryID + "' saving to external json file"));
                            File writeFile = new File(JsonContentLoader.INSTANCE.externalContentFolder.getParent(), "json-gen/" + (entryID + "-recipes.json").replace(":", "_"));
                            if (!writeFile.getParentFile().exists())
                            {
                                writeFile.getParentFile().mkdirs();
                            }
                            try
                            {
                                JsonObject object = new JsonObject();
                                int index = 0;
                                for (IRecipe recipe : recipes)
                                {
                                    try
                                    {
                                        if (recipe instanceof ShapedOreRecipe)
                                        {
                                            int width = 0;
                                            int height = 0;
                                            Object[] recipeItems = null;

                                            Field field = ShapedOreRecipe.class.getDeclaredField("input");
                                            field.setAccessible(true);
                                            recipeItems = (Object[]) field.get(recipe);

                                            field = ShapedOreRecipe.class.getDeclaredField("width");
                                            field.setAccessible(true);
                                            width = field.getInt(recipe);

                                            field = ShapedOreRecipe.class.getDeclaredField("height");
                                            field.setAccessible(true);
                                            height = field.getInt(recipe);


                                            Pair<String, HashMap<String, JsonElement>> itemSet = generateItemData(recipeItems, width, height);

                                            //Build data
                                            if (itemSet != null)
                                            {
                                                JsonObject recipeObject = new JsonObject();
                                                recipeObject.add("type", new JsonPrimitive("shaped"));
                                                recipeObject.add("output", toItemJson(recipe.getRecipeOutput()));
                                                recipeObject.add("grid", new JsonPrimitive(itemSet.left()));

                                                JsonObject itemEntry = new JsonObject();
                                                for (Map.Entry<String, JsonElement> entry : itemSet.right().entrySet())
                                                {
                                                    itemEntry.add(entry.getKey(), entry.getValue());
                                                }
                                                recipeObject.add("items", itemEntry);

                                                object.add("craftingGridRecipe:" + (index++), recipeObject);
                                            }
                                            else
                                            {
                                                sender.addChatMessage(new ChatComponentText("Failed to map recipe items for '" + recipe + "'"));
                                            }
                                        }
                                        else if (recipe instanceof ShapedRecipes)
                                        {
                                            Pair<String, HashMap<String, JsonElement>> itemSet = generateItemData(((ShapedRecipes) recipe).recipeItems, ((ShapedRecipes) recipe).recipeWidth, ((ShapedRecipes) recipe).recipeHeight);

                                            //Build data
                                            if (itemSet != null)
                                            {
                                                JsonObject recipeObject = new JsonObject();
                                                recipeObject.add("type", new JsonPrimitive("shaped"));
                                                recipeObject.add("output", toItemJson(recipe.getRecipeOutput()));
                                                recipeObject.add("grid", new JsonPrimitive(itemSet.left()));

                                                JsonObject itemEntry = new JsonObject();
                                                for (Map.Entry<String, JsonElement> entry : itemSet.right().entrySet())
                                                {
                                                    itemEntry.add(entry.getKey(), entry.getValue());
                                                }
                                                recipeObject.add("items", itemEntry);

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
                                    catch (Exception e)
                                    {
                                        sender.addChatMessage(new ChatComponentText("Error processing recipe '" + recipe + "', see logs for details."));
                                        e.printStackTrace();
                                    }
                                }

                                if (object.entrySet().size() > 0)
                                {
                                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                    try (FileWriter file = new FileWriter(writeFile))
                                    {
                                        file.write(gson.toJson(object));
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

    protected Pair<String, HashMap<String, JsonElement>> generateItemData(Object[] recipeItems, int width, int height)
    {
        HashMap<String, JsonElement> items = new HashMap();
        HashMap<String, String> itemToKey = new HashMap();
        String grid = "";

        int c = 0;
        int w = 0;
        int h = 0;

        //Map items to characters
        boolean upper = false;
        if (recipeItems != null)
        {
            for (Object obj : recipeItems)
            {
                if (c > EnglishLetters.values().length)
                {
                    c = 0;
                    if (!upper)
                    {
                        upper = true;
                    }
                    else
                    {
                        return null;
                    }
                }
                if (obj != null)
                {
                    if (c < EnglishLetters.values().length)
                    {
                        JsonElement element = toItemJson(obj);
                        String json = element.toString();

                        if(itemToKey.containsKey(json))
                        {
                            String key = itemToKey.get(json);
                            grid += key;
                        }
                        else
                        {
                            String ch = EnglishLetters.values()[c].name();
                            ch = upper ? ch : ch.toLowerCase();

                            items.put(ch, element);
                            itemToKey.put(json, ch);

                            grid += ch;
                            c++;
                        }
                    }
                }
                else
                {
                    grid += " ";
                }

                if (w++ >= (width - 1))
                {
                    w = 0;
                    h++;
                    if(h < height)
                    {
                        grid += ",";
                    }
                }
            }
            return new Pair(grid, items);
        }
        return null;
    }

    protected JsonElement toItemJson(Object output)
    {
        if (output instanceof String)
        {
            return new JsonPrimitive((String) output);
        }
        else if (output instanceof Block)
        {
            return new JsonPrimitive("block@" + Block.blockRegistry.getNameForObject(output));
        }
        else if (output instanceof Item)
        {
            return new JsonPrimitive("item@" + Item.itemRegistry.getNameForObject(output));
        }
        else if (output instanceof ItemStack)
        {
            if (((ItemStack) output).getItem() instanceof ItemBlock)
            {
                if (((ItemStack) output).getTagCompound() != null)
                {
                    JsonObject object = new JsonObject();
                    object.add("type", new JsonPrimitive("block"));
                    object.add("value", new JsonPrimitive(Block.blockRegistry.getNameForObject(((ItemBlock) ((ItemStack) output).getItem()).field_150939_a)));
                    object.add("meta", new JsonPrimitive(((ItemStack) output).getItemDamage()));
                    object.add("nbt", JsonConverterNBT.toJson(((ItemStack) output).getTagCompound()));
                    return object;
                }
                return new JsonPrimitive("block@" + Block.blockRegistry.getNameForObject(((ItemBlock) ((ItemStack) output).getItem()).field_150939_a) + "#" + ((ItemStack) output).getItemDamage());
            }
            else
            {
                if (((ItemStack) output).getTagCompound() != null)
                {
                    JsonObject object = new JsonObject();
                    object.add("type", new JsonPrimitive("item"));
                    object.add("value", new JsonPrimitive(Item.itemRegistry.getNameForObject(((ItemStack) output).getItem())));
                    object.add("meta", new JsonPrimitive(((ItemStack) output).getItemDamage()));
                    object.add("nbt", JsonConverterNBT.toJson(((ItemStack) output).getTagCompound()));
                    return object;
                }
                return new JsonPrimitive("item@" + Item.itemRegistry.getNameForObject(((ItemStack) output).getItem()) + "#" + ((ItemStack) output).getItemDamage());
            }
        }
        else if (output instanceof ArrayList)
        {
            ArrayList list = (ArrayList) output;
            HashMap<String, Integer> map = new HashMap();

            for (Object o : list)
            {
                if (o instanceof ItemStack)
                {
                    int[] ids = OreDictionary.getOreIDs((ItemStack) o);
                    for (int id : ids)
                    {
                        String name = OreDictionary.getOreName(id);
                        if (map.containsKey(name))
                        {
                            map.put(name, map.get(name) + 1);
                        }
                        else
                        {
                            map.put(name, 1);
                        }
                    }
                }
            }

            String name = "";
            int n = 0;

            for (Map.Entry<String, Integer> entry : map.entrySet())
            {
                if (entry.getValue() > n)
                {
                    n = entry.getValue();
                    name = entry.getKey();
                }
            }

            return new JsonPrimitive("ore@" + name);
        }
        else
        {
            System.out.println("Could not ID '" + output + "'");
        }
        return new JsonPrimitive("???");
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("generate mod:itemName - generates json recipes using the items existing recipes");
        items.add("generate item@mod:itemName - forces to use item list");
        items.add("generate block@mod:itemName - forces to use block list");
    }
}
