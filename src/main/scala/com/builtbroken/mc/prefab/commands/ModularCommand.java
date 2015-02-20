package com.builtbroken.mc.prefab.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class ModularCommand extends SubCommand
{
    protected ArrayList<AbstractCommand> subCommands = new ArrayList();

    public ModularCommand(String name)
    {
        super(name);
    }

    public ModularCommand addCommand(AbstractCommand command)
    {
        subCommands.add(command);
        if (command instanceof SubCommand)
        {
            ((SubCommand) command).setSuperCommand(this);
        }
        return this;
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if (isHelpCommand(args))
        {
            handleHelp(player, args);
            return true;
        }
        else
        {
            for (AbstractCommand command : subCommands)
            {
                if (command.getCommandName().equalsIgnoreCase(args[0]))
                {
                    String[] a = removeFront(args);
                    if (command.isHelpCommand(a))
                    {
                        command.handleHelp(player, a);
                        return true;
                    }
                    else if (command.handleEntityPlayerCommand(player, a))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        if (isHelpCommand(args))
        {
            handleHelp(sender, args);
            return true;
        }
        else
        {
            for (AbstractCommand command : subCommands)
            {
                if (command.getCommandName().equalsIgnoreCase(args[0]))
                {
                    String[] a = removeFront(args);
                    if (command.isHelpCommand(a))
                    {
                        command.handleHelp(sender, a);
                        return true;
                    }
                    else if (command.handleConsoleCommand(sender, a))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        List<String> commands;
        for (AbstractCommand command : subCommands)
        {
            commands = new ArrayList();
            command.getHelpOutput(sender, commands);
            for (String s : commands)
            {
                items.add(command.getCommandName() + " " + s);
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args != null && args.length > 0 && args[0] != null)
        {
            for (AbstractCommand command : subCommands)
            {
                List l = command.addTabCompletionOptions(sender, removeFront(args));
                if (l != null && !l.isEmpty())
                {
                    return l;
                }
            }
        }
        return null;
    }
}
