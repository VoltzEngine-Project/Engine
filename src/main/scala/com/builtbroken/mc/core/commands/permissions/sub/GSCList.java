package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.ext.GroupSubCommand;
import com.builtbroken.mc.framework.access.AccessGroup;
import com.builtbroken.mc.framework.access.AccessUser;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/18/2015.
 */
public class GSCList extends GroupSubCommand
{
    public GSCList()
    {
        super("list");
    }

    @Override
    public boolean handle(ICommandSender sender, AccessGroup group, String user, String[] args)
    {
        //TODO add page support, so we don't spam the console
        if (args != null && args.length >= 0)
        {
            if (args[0].equalsIgnoreCase("perms"))
            {
                if(group.getNodes() != null && group.getNodes().size() > 0)
                {
                    sender.addChatMessage(new ChatComponentText("==== Permission Nodes ====="));
                    for (String node : group.getNodes())
                    {
                        sender.addChatMessage(new ChatComponentText(node));
                    }
                    sender.addChatMessage(new ChatComponentText(""));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("No perms to list"));
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("users") || args[0].equalsIgnoreCase("members"))
            {
                if(group.getMembers() != null && group.getMembers().size() > 0)
                {
                    sender.addChatMessage(new ChatComponentText("===== Members ====="));
                    for (AccessUser u : group.getMembers())
                    {
                        sender.addChatMessage(new ChatComponentText(u.getName()));
                    }
                    sender.addChatMessage(new ChatComponentText(""));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Group contains no members"));
                }
                return true;
            }
        }
        sender.addChatMessage(new ChatComponentText("Not sure what you want listed"));
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return args != null && args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("perms");
        items.add("users");
        items.add("members");
    }
}
