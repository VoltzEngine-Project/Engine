package com.builtbroken.mc.core.commands.modflags;

import com.builtbroken.mc.core.commands.ext.SubCommandRegion;
import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.lib.modflags.Region;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/23/2015.
 */
public class CommandAddUserToRegion extends SubCommandRegion
{
    public CommandAddUserToRegion()
    {
        super("user");
    }

    @Override
    public boolean handle(ICommandSender sender, Region region, String[] args)
    {
        if (args.length > 0)
        {
            AccessGroup group = null;
            if (args.length > 1)
            {
                String groupName = null;
                if (args[1].equalsIgnoreCase("to") && args.length > 2)
                {
                    if (args[2].equalsIgnoreCase("group"))
                    {
                        if (args.length > 3)
                        {
                            groupName = args[3];
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("Missing group name"));
                            return true;
                        }
                    }
                }
                else if (args[1].equalsIgnoreCase("group"))
                {
                    if (args.length > 2)
                    {
                        groupName = args[2];
                    }
                    else
                    {
                        sender.addChatMessage(new ChatComponentText("Missing group name"));
                        return true;
                    }
                }
                else
                {
                    groupName = args[1];
                }

                group = region.getAccessProfile().getGroup(groupName);
            }
            else
            {
                group = region.getAccessProfile().getGroup("user");
            }
            if (group != null)
            {
                if (group.addMember(args[0]))
                {
                    sender.addChatMessage(new ChatComponentText("User added"));
                }
                else if (group.getMember(args[0]).getGroup() != null)
                {
                    sender.addChatMessage(new ChatComponentText("User is already part of the group"));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Error adding user"));
                }
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Not sure what group you want to add the user to"));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("Missing username"));
        }
        return true;
    }
}
