package com.builtbroken.mc.core.commands.modflags;

import com.builtbroken.mc.core.commands.ext.SubCommandRegion;
import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.lib.modflags.Region;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/23/2015.
 */
public class CommandRemoveUserFromRegion extends SubCommandRegion
{
    public CommandRemoveUserFromRegion()
    {
        super("user");
    }

    @Override
    public boolean handle(ICommandSender sender, Region region, String[] args)
    {
        if(args.length > 0)
        {
            boolean f = false;
            for (AccessGroup group : region.getAccessProfile().getGroups())
            {
                if (group.removeMember(args[0]))
                    f = true;
            }
            if (f)
                sender.addChatMessage(new ChatComponentText("User removed from all groups"));
            else
                sender.addChatMessage(new ChatComponentText("User was not found in any group"));
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("Need a username"));
        }
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }
}
