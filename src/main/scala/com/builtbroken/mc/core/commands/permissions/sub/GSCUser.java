package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.ext.GroupSubCommand;
import com.builtbroken.mc.framework.access.AccessGroup;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/18/2015.
 */
public class GSCUser extends GroupSubCommand
{
    final boolean remove;

    public GSCUser(boolean remove)
    {
        this.remove = remove;
    }

    @Override
    public boolean handle(ICommandSender sender, AccessGroup group, String user, String[] args)
    {
        if(!remove)
        {
            if (group.addMember(user))
            {
                sender.addChatMessage(new ChatComponentText("User added to group"));
            }
            else if (group.getMember(user) != null)
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
            if (group.getMember(user) != null)
            {
                group.removeMember(user);
                sender.addChatMessage(new ChatComponentText("User removed from group"));
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Group does not contain that user"));
            }
        }
        return true;
    }
}
