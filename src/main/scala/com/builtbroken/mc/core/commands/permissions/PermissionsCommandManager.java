package com.builtbroken.mc.core.commands.permissions;

import com.builtbroken.mc.core.Engine;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;

/**
 * Overrides the behavior of MC's default command manager. Allow interception
 * of commands to do permission checking. As well allows OP only commands
 * to be used by any user.
 * Created by robert on 2/17/2015.
 */
public class PermissionsCommandManager extends ServerCommandManager
{
    public boolean hasPermissionForCommand(ICommandSender sender, ICommand command, String[] args)
    {
        return sender instanceof EntityPlayer && Engine.isPlayerOpped((EntityPlayer) sender) || GroupProfileHandler.GLOBAL.canExecuteCommand(sender, command, args);
    }

    @Override
    public ICommand registerCommand(ICommand command)
    {
        if(command!= null && command.getCommandName() != null)
        {
            PermissionsRegistry.handle(command, command.getCommandName());
            return super.registerCommand(command);
        }
        return command;
    }

    @Override
    public int executeCommand(ICommandSender sender, String cmd)
    {
        //Clean up command string
        cmd = cmd.trim();
        if (cmd.startsWith("/"))
        {
            cmd = cmd.substring(1);
        }

        //Get command name and arguments
        String[] args = cmd.split(" ");
        String command_name = args[0];
        args = dropFirstString(args);

        ICommand icommand = (ICommand) this.getCommands().get(command_name);
        int usernameIndex = this.getUsernameIndex(icommand, args);
        int j = 0;
        ChatComponentTranslation chatcomponenttranslation;

        try
        {
            if (icommand == null)
            {
                throw new CommandNotFoundException();
            }

            //Checks if the user can use the command
            if (hasPermissionForCommand(sender, icommand, args))
            {
                CommandEvent event = new CommandEvent(icommand, sender, args);
                if (MinecraftForge.EVENT_BUS.post(event))
                {
                    if (event.exception != null)
                    {
                        throw event.exception;
                    }
                    return 1;
                }

                if (usernameIndex > -1)
                {
                    //Executes the command on several players if listed
                    EntityPlayerMP[] players = PlayerSelector.matchPlayers(sender, args[usernameIndex]);
                    String s2 = args[usernameIndex];
                    int k = players.length;

                    for (int l = 0; l < k; ++l)
                    {
                        EntityPlayerMP entityplayermp = players[l];
                        args[usernameIndex] = entityplayermp.getCommandSenderName();

                        try
                        {
                            icommand.processCommand(sender, args);
                            ++j;
                        } catch (CommandException commandexception1)
                        {
                            ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(commandexception1.getMessage(), commandexception1.getErrorOjbects());
                            chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
                            sender.addChatMessage(chatcomponenttranslation1);
                        }
                    }

                    args[usernameIndex] = s2;
                }
                else
                {
                    try
                    {
                        icommand.processCommand(sender, args);
                        ++j;
                    } catch (CommandException commandexception)
                    {
                        chatcomponenttranslation = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorOjbects());
                        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
                        sender.addChatMessage(chatcomponenttranslation);
                    }
                }
            }
            else
            {
                ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.permission");
                chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
                sender.addChatMessage(chatcomponenttranslation2);
            }
        } catch (WrongUsageException wrongusageexception)
        {
            chatcomponenttranslation = new ChatComponentTranslation("commands.generic.usage", new ChatComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorOjbects()));
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation);
        } catch (CommandException commandexception2)
        {
            chatcomponenttranslation = new ChatComponentTranslation(commandexception2.getMessage(), commandexception2.getErrorOjbects());
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation);
        } catch (Throwable throwable)
        {
            chatcomponenttranslation = new ChatComponentTranslation("commands.generic.exception");
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation);
            Engine.instance.logger().error("Failed to process command: \'" + cmd + "\'", throwable);
        }

        return j;
    }

    private int getUsernameIndex(ICommand command, String[] args)
    {
        if (command == null)
        {
            return -1;
        }
        else
        {
            for (int i = 0; i < args.length; ++i)
            {
                if (command.isUsernameIndex(args, i) && PlayerSelector.matchesMultiplePlayers(args[i]))
                {
                    return i;
                }
            }

            return -1;
        }
    }

    private static String[] dropFirstString(String[] p_71559_0_)
    {
        String[] astring1 = new String[p_71559_0_.length - 1];

        for (int i = 1; i < p_71559_0_.length; ++i)
        {
            astring1[i - 1] = p_71559_0_[i];
        }

        return astring1;
    }
}
