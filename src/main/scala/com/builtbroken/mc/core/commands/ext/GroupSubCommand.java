package com.builtbroken.mc.core.commands.ext;

import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.framework.access.AccessGroup;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/** Prefab for any command which needs to know what group its access first
 * Created by robert on 2/18/2015.
 */
public abstract class GroupSubCommand extends SubCommandWithName
{
    public GroupSubCommand()
    {
        this("group");
    }

    public GroupSubCommand(String name)
    {
        super(name);
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
                return handle(sender, GroupProfileHandler.GLOBAL.getAccessProfile().getGroup(name), user, removeFront(args));
            }
            else
            {
                sender.sendMessage(new TextComponentString("Unknown group"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentString("Missing group name"));
        }
        return true;
    }

    public abstract boolean handle(ICommandSender sender, AccessGroup group, String user, String[] args);

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("[name]");
    }
}
