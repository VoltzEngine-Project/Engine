package com.builtbroken.mc.prefab.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class AbstractCommand extends CommandBase
{
    protected final String name;

    public AbstractCommand(String name)
    {
        this.name = name;
    }

    @Override
    public String getCommandName()
    {
        return name;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if(!handleHelp(sender, args))
        {
            if (!(sender instanceof EntityPlayer && handleEntityPlayerCommand((EntityPlayer) sender, args)) && !handleConsoleCommand(sender, args))
            {
                sender.addChatMessage(new ChatComponentText("Error: Unknown chat command"));
            }
        }
    }

    public boolean isHelpCommand(String[] args)
    {
        return args == null || args.length == 0 || args[0] == null || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?");
    }

    public boolean handleHelp(ICommandSender sender, String[] args)
    {
        if (isHelpCommand(args))
        {
            int p = 0;
            if (args != null && args.length >= 2)
            {
                try
                {
                    p = Integer.parseInt(args[1]);
                }
                catch (NumberFormatException e)
                {

                }
            }
            printHelp(sender, p);
            return true;
        }
        return false;
    }

    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return false;
    }

    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        return false;
    }

    protected void printHelp(ICommandSender sender, int p)
    {
        List<String> items = new ArrayList();
        getHelpOutput(sender, items);

        sender.addChatMessage(new ChatComponentText("====== help -" + getPrefix().replace("/", "") + "- page " + p +" ======"));
        for(int i = 0 + (p * 10); i < 10 + (p * 10) && i < items.size(); i++)
        {
            sender.addChatMessage(new ChatComponentText(getPrefix() + " " + items.get(i)));
        }
        sender.addChatMessage(new ChatComponentText(""));
    }

    public String getPrefix()
    {
        return "/" + getCommandName();
    }

    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("help");
    }

    protected final String[] playersOnlineByUsername()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    protected String[] removeFront(String[] array)
    {
        return removeFront(array, 1);
    }

    protected String[] removeFront(String[] array, int count)
    {
        if(count  <= 0)
            count = 1;

        if (array.length > count)
        {
            String[] a = new String[array.length - count];
            for (int i = 0; i < a.length; i++)
            {
                a[i] = array[i + count];
            }
            return a;
        }
        return null;
    }
}
