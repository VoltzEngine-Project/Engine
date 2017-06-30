package com.builtbroken.mc.core.commands.thread;

import com.builtbroken.mc.api.VoltzEngineAPI;
import com.builtbroken.mc.api.process.IWorkerThread;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandThreadClear extends SubCommand
{
    public CommandThreadClear()
    {
        super("clear");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handleConsoleCommand(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        for (IWorkerThread thread : VoltzEngineAPI.WORKER_THREADS.values())
        {
            if (thread != null)
            {
                int process = thread.containedProcesses();
                thread.clearProcesses();
                sender.addChatMessage(new ChatComponentText("Cleared " + process + " process from thread " + thread.toString()));
            }
        }
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return args != null && args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }
}
