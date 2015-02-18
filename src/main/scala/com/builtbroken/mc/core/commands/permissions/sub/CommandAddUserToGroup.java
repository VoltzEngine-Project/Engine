package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.SubCommandUser;
import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.lib.access.AccessGroup;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/18/2015.
 */
public class CommandAddUserToGroup extends SubCommandUser
{
    public CommandAddUserToGroup()
    {
        super("group");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String user, String[] args)
    {
        return handleConsoleCommand(player, user, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String user, String[] args)
    {
        if (args.length > 0)
        {
            String name = args[0];
            if (GroupProfileHandler.GLOBAL.getAccessProfile().getGroup(name) != null)
            {
                AccessGroup group = new AccessGroup(name);
                if (group.addMember(user))
                {
                    sender.addChatMessage(new ChatComponentText("User added to group"));
                }
                else if(group.getMember(user) != null)
                {
                    sender.addChatMessage(new ChatComponentText("Error: User already added"));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Error adding user to group"));
                }
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Unknown group"));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("Missing group name"));
        }
        return true;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("[name]");
    }
}
