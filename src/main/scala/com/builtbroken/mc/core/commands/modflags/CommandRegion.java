package com.builtbroken.mc.core.commands.modflags;

import com.builtbroken.mc.core.commands.ext.SubCommandRegion;
import com.builtbroken.mc.lib.modflags.Region;
import com.builtbroken.mc.lib.modflags.RegionManager;
import com.builtbroken.mc.prefab.commands.AbstractCommand;
import com.builtbroken.mc.prefab.commands.ModularCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/18/2015.
 */
public class CommandRegion extends ModularCommand
{
    public CommandRegion()
    {
        super("region");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if(args.length > 0)
        {
            String name = args[0];
            Region region = RegionManager.getControllerForWorld(player.worldObj).getRegion(name);
            if (region != null)
            {
                if (args.length > 1)
                {
                    for (AbstractCommand command : subCommands)
                    {
                        if (command instanceof SubCommandRegion && command.getCommandName().equalsIgnoreCase(args[1]))
                        {
                            if (((SubCommandRegion) command).handle(player, region, removeFront(args, 2)))
                            {
                                return true;
                            }
                        }
                    }
                }
                player.addChatMessage(new ChatComponentText("Unknown region sub command"));
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
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {

        return true;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        List<String> commands;
        for (AbstractCommand command : subCommands)
        {
            commands = new ArrayList();
            command.getHelpOutput(sender, commands);
            for (String s : commands)
            {
                items.add("[name] " + command.getCommandName() + " " + s);
            }
        }
    }
}
