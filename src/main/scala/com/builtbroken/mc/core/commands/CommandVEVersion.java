package com.builtbroken.mc.core.commands;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.prefab.commands.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVEVersion extends SubCommand
{
    public CommandVEVersion()
    {
        super("version");
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        sender.addChatMessage(new ChatComponentText("Version: " + References.VERSION + "  Build: " + References.BUILD_VERSION));
        return true;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("");
    }
}
