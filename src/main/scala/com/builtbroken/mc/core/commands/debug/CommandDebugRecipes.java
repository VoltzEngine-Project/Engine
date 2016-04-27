package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.prefab.commands.SubCommand;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import com.builtbroken.mc.prefab.tile.BlockTile;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Used to test for recipe conflicts and dump items without recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugRecipes extends SubCommand
{
    public CommandDebugRecipes()
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
            String modID = args[0];
            if (Loader.isModLoaded(modID))
            {
                sender.addChatMessage(new ChatComponentText("Checking data...."));

                List<Item> items = InventoryUtility.getItemsForMod(modID);
                if (items != null && !items.isEmpty())
                {
                    HashMap<ItemStackWrapper, List<IRecipe>> recipeMap = new HashMap();
                    HashMap<Item, List<IRecipe>> itemToRecipes = new HashMap();

                    sender.addChatMessage(new ChatComponentText("Found " + items.size() + " items for the mod " + modID + " moving on to processing recipes"));

                    for (Object r : CraftingManager.getInstance().getRecipeList())
                    {
                        if (r instanceof IRecipe)
                        {
                            if (((IRecipe) r).getRecipeOutput() != null && items.contains(((IRecipe) r).getRecipeOutput().getItem()))
                            {
                                ItemStackWrapper wrapper = new ItemStackWrapper(((IRecipe) r).getRecipeOutput());
                                List<IRecipe> list = recipeMap.get(wrapper);
                                if (list == null)
                                {
                                    list = new ArrayList();
                                }
                                list.add((IRecipe) r);
                                recipeMap.put(wrapper, list);
                                itemToRecipes.put(((IRecipe) r).getRecipeOutput().getItem(), list);

                            }
                        }
                    }
                    sender.addChatMessage(new ChatComponentText("Mapped " + recipeMap.size() + " entries with recipes"));
                    if (args.length == 1 || args[1].equalsIgnoreCase("conflict"))
                    {
                        sender.addChatMessage(new ChatComponentText("Not implemented yet"));
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("missing"))
                    {
                        //TODO add handling for subtypes
                        for (Item item : items)
                        {
                            if (!itemToRecipes.containsKey(item))
                            {
                                if (item instanceof ItemBlock)
                                {
                                    Block block = ((ItemBlock) item).field_150939_a;
                                    if (block instanceof BlockTile)
                                    {
                                        sender.addChatMessage(new ChatComponentText("Tile[" + ((BlockTile) block).staticTile.name + "] has no recipes for any subtype"));
                                    }
                                    else
                                    {
                                        sender.addChatMessage(new ChatComponentText("Block[" + block.getLocalizedName() + "] has no recipes for any subtype"));
                                    }
                                }
                                else
                                {
                                    sender.addChatMessage(new ChatComponentText("Item[" + item.getItemStackDisplayName(new ItemStack(item)) + "] has no recipes for any subtype"));
                                }
                            }
                        }
                    }
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("No items are mapped for the mod[" + modID + "]"));
                }
                return true;
            }
            else
            {
                //TODO maybe show closest spelling
                sender.addChatMessage(new ChatComponentText("Failed to find mod[" + modID + "]"));
                return true;
            }
        }
        return handleHelp(sender, args);
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("<modId> <missing/conflict>");
    }
}
