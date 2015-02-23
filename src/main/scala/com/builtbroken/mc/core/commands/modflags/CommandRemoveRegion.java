package com.builtbroken.mc.core.commands.modflags;

import com.builtbroken.mc.core.commands.ext.SubCommandRegion;
import com.builtbroken.mc.lib.modflags.Region;
import com.builtbroken.mc.lib.modflags.RegionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandRemoveRegion extends SubCommandRegion
{
    @Override
    public boolean handle(ICommandSender sender, Region region, String[] args)
    {
        if(sender.getEntityWorld() != null)
        {
            if(RegionManager.getControllerForWorld(sender.getEntityWorld()).removeRegion(region))
            {
                sender.addChatMessage(new ChatComponentText("Region removed"));
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Error removing region"));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("You need to be in the same world as the region to remove it"));
        }
        return true;
    }
}
