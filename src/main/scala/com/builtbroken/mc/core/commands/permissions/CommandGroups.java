package com.builtbroken.mc.core.commands.permissions;

import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.prefab.commands.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/18/2015.
 */
public class CommandGroups extends SubCommand
{
    public CommandGroups()
    {
        super("groups");
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
        else if (args[0].equalsIgnoreCase("list"))
        {
            sender.addChatMessage(new ChatComponentText("==== Groups ===="));
            for (AccessGroup group : CommandPermissionHandler.GLOBAL.getAccessProfile().getGroups())
            {
                sender.addChatMessage(new ChatComponentText("  Group[" + group.getName() + (group.getExtendGroupName() != null ? " extends " + group.getExtendGroupName() : "") + "]  Members: " + group.getMembers().size()));
            }
            sender.addChatMessage(new ChatComponentText("================"));
        }
        return true;
    }
}
