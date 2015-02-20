package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.ext.GroupSubCommand;
import com.builtbroken.mc.core.commands.ext.UserSubCommand;
import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.lib.access.AccessUser;
import com.builtbroken.mc.prefab.commands.AbstractCommand;
import com.builtbroken.mc.prefab.commands.ModularCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/20/2015.
 */
public class CommandUser extends ModularCommand
{
    public CommandUser()
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
        if(isHelpCommand(args))
        {
            handleHelp(sender, args);
        }
        else
        {
            String name = args[0];
            AccessUser user = GroupProfileHandler.GLOBAL.getAccessProfile().getUserAccess(name);
            if (user != null && user.getGroup() != null)
            {
                if (args.length > 1)
                {
                    for (AbstractCommand command : subCommands)
                    {
                        if (command instanceof UserSubCommand && command.getCommandName().equalsIgnoreCase(args[1]))
                        {
                            if (((UserSubCommand) command).handle(sender, user, removeFront(args, 2)))
                            {
                                return true;
                            }
                        }
                    }
                }
                sender.addChatMessage(new ChatComponentText("Unknown user sub command"));
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("User not found in permissions profile"));
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
                items.add("[name] " + command.getCommandName() + " " +  s);
            }
        }
    }
}
