package com.builtbroken.mc.core.commands.ext;

import com.builtbroken.mc.lib.modflags.Region;
import com.builtbroken.mc.lib.modflags.RegionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/22/2015.
 */
public abstract class SubCommandRegion extends SubCommandWithName
{
    public SubCommandRegion()
    {
        this("region");
    }

    public SubCommandRegion(String name)
    {
        super(name);
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String user, String[] args)
    {
        if (args.length > 0)
        {
            String name = args[0];
            Region region = RegionManager.getControllerForWorld(player.worldObj).getRegion(name);
            if (region != null)
            {
                return handle(player, region, user, removeFront(args));
            }
            else
            {
                player.addChatMessage(new ChatComponentText("Unknown region"));
            }
        }
        else
        {
            player.addChatMessage(new ChatComponentText("Missing region name"));
        }
        return true;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String user, String[] args)
    {
        sender.addChatMessage(new ChatComponentText("This command can't be used from console"));
        return true;
    }

    public abstract boolean handle(ICommandSender sender, Region region, String user, String[] args);

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("[name]");
    }
}
