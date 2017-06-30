package com.builtbroken.mc.core.commands.ext;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by robert on 2/18/2015.
 */
public class SubCommandWithName extends SubCommand
{
    public SubCommandWithName(String name)
    {
        super(name);
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return args.length > 0 && handleEntityPlayerCommand(player, args[0], removeFront(args));
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        return args.length > 0 && handleConsoleCommand(sender, args[0], removeFront(args));
    }

    public boolean handleEntityPlayerCommand(EntityPlayer player, String user, String[] args)
    {
        return false;
    }

    public boolean handleConsoleCommand(ICommandSender sender, String user, String[] args)
    {
        return false;
    }
}
