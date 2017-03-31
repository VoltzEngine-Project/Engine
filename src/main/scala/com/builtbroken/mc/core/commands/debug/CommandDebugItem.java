package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugItem extends SubCommand
{
    public CommandDebugItem()
    {
        super("item");
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
            if (args[0].equals("dump") || args[0].equals("dumpsubs"))
            {
                sender.addChatMessage(new ChatComponentText("See console for data"));
                FMLControlledNamespacedRegistry<Item> registry = (FMLControlledNamespacedRegistry<Item>) Item.itemRegistry;
                Set set = registry.getKeys();
                for (Object obj : set)
                {
                    String name = (String) obj;
                    Item item = registry.getObject(name);
                    System.out.println("" + name + "  " + item);
                    if (item != null)
                    {
                        if (args[0].equals("dumpsubs"))
                        {
                            List list = new ArrayList();
                            item.getSubItems(item, item.getCreativeTab(), list);
                            for (Object o : list)
                            {
                                System.out.println("\t" + o);
                                if (o instanceof ItemStack)
                                {
                                    ItemStack stack = (ItemStack) o;
                                    System.out.println("\t\tName: " + stack.getUnlocalizedName());
                                    int[] oreIDs = OreDictionary.getOreIDs(stack);
                                    if (oreIDs != null && oreIDs.length > 0)
                                    {
                                        for (int i : oreIDs)
                                        {
                                            String oreName = OreDictionary.getOreName(i);
                                            System.out.println("\t\t" + oreName);
                                        }
                                    }
                                }
                            }
                        }
                    }
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
        items.add("<modId> <missing/conflict>");
    }
}
