package universalelectricity.core

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.util.ChatComponentText
import java.util.List
import universalelectricity.core.grid.UpdateTicker
import universalelectricity.api.UniversalElectricity

object UECommand extends CommandBase
{
  def getCommandName: String =
  {
    return "ue"
  }

  def getCommandUsage(par1ICommandSender: ICommandSender): String =
  {
    return "/ue help"
  }

  def processCommand(sender: ICommandSender, args: Array[String])
  {
    try
    {
      if (args == null || args.length == 0 || args(0).equalsIgnoreCase("help"))
      {
        sender.addChatMessage(new ChatComponentText("/ue version"))
        sender.addChatMessage(new ChatComponentText("/ue gridinfo"))
        sender.addChatMessage(new ChatComponentText("/ue gridpause"))
        return
      }
      if (args(0).equalsIgnoreCase("version"))
      {
        sender.addChatMessage(new ChatComponentText("Universal Electricity Version: " + UniversalElectricity.VERSION))
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
    }
    catch
      {
        case e: Exception =>
        {
        }
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