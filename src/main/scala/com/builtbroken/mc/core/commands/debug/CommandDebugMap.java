package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Used to test for recipe conflicts and dump items without recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugMap extends SubCommand
{
    public CommandDebugMap()
    {
        super("map");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equalsIgnoreCase("radar"))
            {

            }
            else if (args[0].equalsIgnoreCase("heat"))
            {

            }
        }
        return handleHelp(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        return false; //TODO implement console version
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {

    }
}
