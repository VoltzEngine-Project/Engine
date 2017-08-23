package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.ext.GroupSubCommand;
import com.builtbroken.mc.framework.access.AccessGroup;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

/**
 * Handles adding perms to a group
 * Created by robert on 2/18/2015.
 */
public class GSCPerm extends GroupSubCommand
{
    final boolean remove;

    public GSCPerm(boolean remove)
    {
        this.remove = remove;
    }

    @Override
    public boolean handle(ICommandSender sender, AccessGroup group, String node, String[] args)
    {
        if (!remove && group.hasNode(node) || remove && group.hasExactNode(node))
        {
            if (remove)
            {
                group.removeNode(node);
            }
            else
            {
                sender.sendMessage(new TextComponentString("Group already contains the node or super version of the node"));
            }
        }
        else
        {
            if(!remove)
            {
                group.addNode(node);
                sender.sendMessage(new TextComponentString("Node added to the group"));
            }
            else if(group.hasNode(node))
            {
                sender.sendMessage(new TextComponentString("Group contains a super version of that node, but not an exact match"));
            }
            else if(group.getExtendGroup() != null && group.getExtendGroup().hasNode(node))
            {
                sender.sendMessage(new TextComponentString("Super group contains the node, but the node is not contained in this group"));
            }
            else
            {
                sender.sendMessage(new TextComponentString("Group does not contain the node"));
            }
        }
        return true;
    }
}
