package com.builtbroken.mc.core.commands.json;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.lib.json.processors.recipe.crafting.JsonCraftingRecipeData;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

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
                String entryID = args[1];
                ItemStack stack = new JsonCraftingRecipeData(null, null, null, false, false).toStack(entryID);
                if (stack != null)
                {
                    List<IRecipe> recipes = InventoryUtility.getRecipesWithOutput(stack);
                    sender.addChatMessage(new ChatComponentText("Found " + recipes.size() + " for '" + entryID + "' saving to external json file"));
                    for (IRecipe recipe : recipes)
                    {
                        if (recipe instanceof ShapedOreRecipe)
                        {

                        }
                        else if (recipe instanceof ShapedRecipes)
                        {

                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("Failed to ID recipe type '" + recipe + "'"));
                        }
                    }
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Failed to locate entry for '" + entryID + "'"));
                }
                return true;
            }
        }
        else
        {
            return handleHelp(sender, args);
        }
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("generate mod:itemName - generates json recipes using the items existing recipes");
        items.add("generate item@mod:itemName - forces to use item list");
        items.add("generate block@mod:itemName - forces to use block list");
    }
}
