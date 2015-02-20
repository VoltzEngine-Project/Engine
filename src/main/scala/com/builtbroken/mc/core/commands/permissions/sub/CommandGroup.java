package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.ext.GroupSubCommand;
import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.prefab.commands.AbstractCommand;
import com.builtbroken.mc.prefab.commands.ModularCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/18/2015.
 */
public class CommandGroup extends ModularCommand
{
    public CommandGroup()
    {
        super("group");
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
            AccessGroup group = GroupProfileHandler.GLOBAL.getAccessProfile().getGroup(name);
            if (group != null)
            {
                if (args.length > 1)
                {
                    for (AbstractCommand command : subCommands)
                    {
                        if (command instanceof GroupSubCommand && command.getCommandName().equalsIgnoreCase(args[1]))
                        {
                            if (((GroupSubCommand) command).handle(sender, GroupProfileHandler.GLOBAL.getAccessProfile().getGroup(name), "", removeFront(args, 2)))
                            {
                                return true;
                            }
                        }
                    }
                }
                sender.addChatMessage(new ChatComponentText("Unknown group sub command"));
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Unknown group"));
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
