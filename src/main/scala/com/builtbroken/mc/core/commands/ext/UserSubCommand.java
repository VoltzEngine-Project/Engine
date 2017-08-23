package com.builtbroken.mc.core.commands.ext;

import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.framework.access.AccessUser;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/** Prefab for any command which needs to know what group its access first
 * Created by robert on 2/18/2015.
 */
public abstract class UserSubCommand extends SubCommandWithName
{
    public UserSubCommand()
    {
        this("user");
    }

    public UserSubCommand(String name)
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
            AccessUser accessUser = GroupProfileHandler.GLOBAL.getAccessProfile().getUserAccess(user);
            if (accessUser != null && accessUser.getGroup() != null)
            {
                return handle(sender, accessUser, removeFront(args));
            }
            else
            {
                sender.sendMessage(new TextComponentString("User not found in permissions profile"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentString("Missing user name"));
        }
        return true;
    }

    public abstract boolean handle(ICommandSender sender, AccessUser user, String[] args);

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("[name]");
    }
}
