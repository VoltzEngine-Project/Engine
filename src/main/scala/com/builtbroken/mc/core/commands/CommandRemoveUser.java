package com.builtbroken.mc.core.commands;

import com.builtbroken.mc.prefab.commands.AbstractCommand;
import com.builtbroken.mc.prefab.commands.ModularCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Command designed to add users to some group. Uses sub commands
 * to allow support of any group type. Examples are permission groups,
 * and region groups.
 * <p/>
 * Created by robert on 2/18/2015.
 */
public class CommandRemoveUser extends ModularCommand
{
    public CommandRemoveUser()
    {
        super("user");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handleConsoleCommand(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        if (isHelpCommand(args))
        {
            handleHelp(sender, args);
        }
        else
        {
            String username = args[0];
            if (username != null && !username.isEmpty())
            {
                if (args.length > 1)
                {
                    String command = args[1];
                    String[] a = removeFront(args, 2);
                    if (command.equalsIgnoreCase("from"))
                    {
                        if (args.length > 2)
                        {
                            command = args[2];
                            a = removeFront(a);
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("Need to know what to remove the user from"));
                            return true;
                        }
                    }
                    for (AbstractCommand c : subCommands)
                    {
                        if (c instanceof SubCommandUser && c.getCommandName().equalsIgnoreCase(command) && ((SubCommandUser) c).handleConsoleCommand(sender, username, a))
                        {
                            return true;
                        }
                    }
                    sender.addChatMessage(new ChatComponentText("Unknown sub command"));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Need to know what to remove the user from"));
                }
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Empty user names are not permitted"));
            }
        }
        return true;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        List<String> commands;
        for (AbstractCommand command : subCommands)
        {
            commands = new ArrayList();
            command.getHelpOutput(sender, commands);
            for(String s : commands)
            {
                items.add(command.getCommandName() + " [username] from " + s);
            }
        }
    }
}
