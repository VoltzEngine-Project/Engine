package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugClass extends SubCommand
{
    public CommandDebugClass()
    {
        super("class");
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
            String clazzString = args[0];

            try
            {
                Class clazz = Class.forName(clazzString);
                //TODO print clazz bytes
            }
            catch (ClassNotFoundException e)
            {
                sender.addChatMessage(new ChatComponentText("Could not find class for '" + clazzString + "'"));
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
        items.add("<class> - dumps class file to game folder in order to check for ASM errors");
    }
}
