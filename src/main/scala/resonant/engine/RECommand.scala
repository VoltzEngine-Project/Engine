package resonant.engine

import java.util.List

import net.minecraft.command.{CommandBase, ICommandSender, WrongUsageException}
import net.minecraft.util.ChatComponentText
import universalelectricity.core.grid.UpdateTicker

object RECommand extends CommandBase
{
  override def getCommandName: String =
  {
    return "ue"
  }

  override def getCommandUsage(par1ICommandSender: ICommandSender): String =
  {
    return "/ue help"
  }

  override def processCommand(sender: ICommandSender, args: Array[String])
  {
      if (args == null || args.length == 0 || args(0).equalsIgnoreCase("help"))
      {
        sender.addChatMessage(new ChatComponentText("/" + getCommandName + " version"))
        sender.addChatMessage(new ChatComponentText("/" + getCommandName + " gridinfo"))
        sender.addChatMessage(new ChatComponentText("/" + getCommandName + " gridpause"))
        return
      }
      if (args(0).equalsIgnoreCase("version"))
      {
        sender.addChatMessage(new ChatComponentText("Version: " + References.VERSION +"  Build: " + References.BUILD_VERSION))
      }
      if (args(0).equalsIgnoreCase("gridinfo"))
      {
        sender.addChatMessage(new ChatComponentText("[Universal Electricity Grid] Tick rate: " + (if (UpdateTicker.pause) "Paused" else (if (UpdateTicker.getDeltaTime > 0) 1 / UpdateTicker.getDeltaTime.asInstanceOf[Double] else 0) * 1000 + "/s")))
        sender.addChatMessage(new ChatComponentText("[Universal Electricity Grid] Grids running: " + UpdateTicker.getUpdaterCount))
        return
      }
      if (args(0).equalsIgnoreCase("gridpause"))
      {
        UpdateTicker.pause = !UpdateTicker.pause
        sender.addChatMessage(new ChatComponentText("[Universal Electricity Grid] Ticking grids running state: " + !UpdateTicker.pause))
        return
      }

        throw new WrongUsageException(this.getCommandUsage(sender))
  }

  override def getRequiredPermissionLevel: Int =
  {
    return 0
  }

  override def addTabCompletionOptions(sender: ICommandSender, args: Array[String]): List[_] =
  {
    return if (args.length == 1) CommandBase.getListOfStringsMatchingLastWord(args, "tps") else null
  }
}