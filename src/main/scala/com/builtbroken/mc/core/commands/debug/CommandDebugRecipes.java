package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.prefab.commands.SubCommand;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
                FMLControlledNamespacedRegistry<Item> registry = (FMLControlledNamespacedRegistry<Item>) Item.itemRegistry;
                Set set = registry.getKeys();

                List<Item> items = new ArrayList();
                for (Object obj : set)
                {
                    if (obj instanceof String && ((String) obj).startsWith(modID))
                    {
                        Object entry = registry.getObject(obj);
                        if (entry instanceof Item)
                        {
                            items.add((Item) entry);
                        }
                    }
                }
                HashMap<ItemStackWrapper, List<IRecipe>> recipeMap = new HashMap();
                sender.addChatMessage(new ChatComponentText("Found " + items.size() + " items for the mod " + modID + " moving on to processing recipes"));
                for (Object r : CraftingManager.getInstance().getRecipeList())
                {
                    if (r instanceof IRecipe)
                    {
                        if (items.contains(((IRecipe) r).getRecipeOutput()))
                        {
                            ItemStackWrapper wrapper = new ItemStackWrapper(((IRecipe) r).getRecipeOutput());
                            List<IRecipe> list = recipeMap.get(wrapper);
                            if (list == null)
                            {
                                list = new ArrayList();
                            }
                            list.add((IRecipe) r);
                            recipeMap.put(wrapper, list);
                        }
                    }
                }
                sender.addChatMessage(new ChatComponentText("Mapped " + recipeMap.size() + " entries with recipes"));
                if (args.length == 1 || args[1].equalsIgnoreCase("conflict"))
                {

                    return true;
                }
                else if (args[1].equalsIgnoreCase("missing"))
                {
                    return true;
                }
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
