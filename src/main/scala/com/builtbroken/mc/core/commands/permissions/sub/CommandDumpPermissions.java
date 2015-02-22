package com.builtbroken.mc.core.commands.permissions.sub;

import com.builtbroken.mc.core.commands.permissions.PermissionsRegistry;
import com.builtbroken.mc.prefab.commands.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandDumpPermissions extends SubCommand
{
    public CommandDumpPermissions()
    {
        super("permissions");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handleConsoleCommand(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        sender.addChatMessage(new ChatComponentText("Dumping permission nodes to file in the server's base directory"));
        PermissionsRegistry.dumpNodesToFile();
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return false;
    }
}
