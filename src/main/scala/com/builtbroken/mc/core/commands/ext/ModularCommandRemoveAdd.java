package com.builtbroken.mc.core.commands.ext;

import com.builtbroken.mc.core.commands.prefab.AbstractCommand;
import com.builtbroken.mc.core.commands.prefab.ModularCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

/** Prefab designed to be used as a super command for a series of remove or add sub commands.
 * For example /command remove perm mc.help from group user
 * This prefab would fall in place as the perm command that is a sub command of remove. In which
 * this command will pass the permission node to the group command. Which then will remove
 * the node from group user.
 *
 * Created by robert on 2/18/2015.
 */
public class ModularCommandRemoveAdd extends ModularCommand
{
    final boolean remove;
    final String type;

    public ModularCommandRemoveAdd(String name, String type, boolean remove)
    {
        super(name);
        this.type = type;
        this.remove = remove;
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
                    if (!remove && command.equalsIgnoreCase("to") || remove && command.equalsIgnoreCase("from"))
                    {
                        if (args.length > 2)
                        {
                            command = args[2];
                            a = removeFront(a);
                        }
                        else if(remove)
                        {
                            sender.sendMessage(new TextComponentString("Need to know what to remove the " + type + " from"));
                            return true;
                        }
                        else
                        {
                            sender.sendMessage(new TextComponentString("Need to know what to add the " + type + " to"));
                            return true;
                        }
                    }
                    for (AbstractCommand c : subCommands)
                    {
                        if (c instanceof SubCommandWithName && c.getName().equalsIgnoreCase(command) && ((SubCommandWithName) c).handleConsoleCommand(sender, username, a))
                        {
                            return true;
                        }
                    }
                    sender.sendMessage(new TextComponentString("Unknown sub command"));
                }
                else if(remove)
                {
                    sender.sendMessage(new TextComponentString("Need to know what to remove the " + type + " from"));
                    return true;
                }
                else
                {
                    sender.sendMessage(new TextComponentString("Need to know what to add the " + type + " to"));
                    return true;
                }
            }
            else
            {
                sender.sendMessage(new TextComponentString("Empty names are not permitted"));
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
                items.add("[" + type + "] " + (remove ? "from" : "to") + " " + command.getName() + " " + s);
            }
        }
    }
}
