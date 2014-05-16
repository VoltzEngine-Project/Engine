package resonant.lib.grid;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;

public class UECommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "ue";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/ue help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        try
        {
            if (args == null || args.length == 0 || args[0].equalsIgnoreCase("help"))
            {
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("/ue gridinfo"));
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("/ue gridpause"));
                return;
            }

            if (args[0].equalsIgnoreCase("gridinfo"))
            {
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("[Universal Electricity Grid] Tick rate: " + (UpdateTicker.INSTANCE.pause ? "Paused" : (UpdateTicker.INSTANCE.getDeltaTime() > 0 ? 1 / (double) UpdateTicker.INSTANCE.getDeltaTime() : 0) * 1000 + "/s")));
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("[Universal Electricity Grid] Grids running: " + UpdateTicker.INSTANCE.getUpdaterCount()));
                return;
            }

            if (args[0].equalsIgnoreCase("gridpause"))
            {
                UpdateTicker.INSTANCE.pause = !UpdateTicker.INSTANCE.pause;
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("[Universal Electricity Grid] Ticking grids running state: " + !UpdateTicker.INSTANCE.pause));
                return;
            }
        }
        catch (Exception e)
        {
        }

        throw new WrongUsageException(this.getCommandUsage(sender));
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "tps" }) : null;
    }

    @Override
    public int compareTo(Object par1Obj)
    {
        return compareTo((ICommand) par1Obj);
    }
}
