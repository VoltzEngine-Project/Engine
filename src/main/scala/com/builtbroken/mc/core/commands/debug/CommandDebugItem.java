package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugItem extends SubCommand
{
    public CommandDebugItem()
    {
        super("item");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handleConsoleCommand(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equals("dump") || args[0].equals("dumpsubs"))
            {
                sender.sendMessage(new TextComponentString("Not implemented"));
                return true;
            }
        }
        else
        {
            return handleHelp(sender, args);
        }
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("<modId> <missing/conflict>");
    }
}
