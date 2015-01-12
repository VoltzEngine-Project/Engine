package com.builtbroken.mc.core

import java.util.List

import net.minecraft.command.{CommandBase, ICommandSender, WrongUsageException}
import net.minecraft.util.ChatComponentText

object RECommand extends CommandBase
{
  override def getCommandName: String = "ue"

  override def getCommandUsage(par1ICommandSender: ICommandSender): String = "/ue help"

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

        throw new WrongUsageException(this.getCommandUsage(sender))
  }

  override def getRequiredPermissionLevel: Int = 0

  override def addTabCompletionOptions(sender: ICommandSender, args: Array[String]): List[_] =
  {
    return if (args.length == 1) CommandBase.getListOfStringsMatchingLastWord(args, "tps") else null
  }
}