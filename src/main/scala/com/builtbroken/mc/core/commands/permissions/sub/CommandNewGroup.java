package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.prefab.commands.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandNewGroup extends SubCommand
{
    public CommandNewGroup()
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
        if (args.length > 0)
        {
            String name = args[0];
            if (GroupProfileHandler.GLOBAL.getAccessProfile().getGroup(name) == null)
            {
                AccessGroup toExtend = null;
                if (args.length > 1 && args[1].equalsIgnoreCase("extend"))
                {
                    if (args.length > 2)
                    {
                        String extendName = args[2];
                        toExtend = GroupProfileHandler.GLOBAL.getAccessProfile().getGroup(extendName);
                    }
                    if (toExtend == null)
                    {
                        sender.addChatMessage(new ChatComponentText("Error: Unknown group to extend"));
                        return true;
                    }
                }

                AccessGroup group = new AccessGroup(name);
                if (toExtend != null)
                {
                    group.setToExtend(toExtend);
                }
                if (GroupProfileHandler.GLOBAL.getAccessProfile().addGroup(group))
                {
                    sender.addChatMessage(new ChatComponentText("Group added"));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Error adding group"));
                }
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("A Group by that name already exists"));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("Missing group name"));
        }
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
        items.add("[name]");
        items.add("[name] extend [group_to_extend]");
    }
}
