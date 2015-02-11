package com.builtbroken.mc.prefab.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

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
        if(command instanceof SubCommand)
        {
            ((SubCommand) command).setSuperCommand(this);
        }
        return this;
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        for (AbstractCommand command : subCommands)
        {
            if (command.getCommandName().equalsIgnoreCase(args[0]) && command.handleEntityPlayerCommand(player, removeFront(args)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        for (AbstractCommand command : subCommands)
        {
            if (command.getCommandName().equalsIgnoreCase(args[0]) && command.handleConsoleCommand(sender, removeFront(args)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void printHelp(ICommandSender sender, int p)
    {
        List<String> items = new ArrayList();
        items.add("====== help -" + (super_command != null ? super_command.getCommandName() : "") + " " +getCommandName() + "- page " + p +" ======");
        getHelpOutput(sender, items);
        items.add("");

        for(int i = 0 + (p * 10); i < 10 + (p * 10) && i < items.size(); i++)
        {
            sender.addChatMessage(new ChatComponentText("/" + (super_command != null ? super_command.getCommandName() : "") + items.get(i)));
        }
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        for (AbstractCommand command : subCommands)
        {
            command.getHelpOutput(sender, items);
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
                if(l != null && !l.isEmpty())
                {
                    return l;
                }
            }
        }
        return null;
    }
}
