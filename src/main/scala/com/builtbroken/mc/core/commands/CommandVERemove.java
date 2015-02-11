package com.builtbroken.mc.core.commands;

import com.builtbroken.mc.prefab.commands.SubCommand;
import com.builtbroken.mc.prefab.entity.selector.EntitySelector;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVERemove extends SubCommand
{
    public CommandVERemove()
    {
        super("remove");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args)
    {
        if (isHelpCommand(args))
        {
            int p = 0;
            if (args.length >= 2)
            {
                try
                {
                    p = Integer.parseInt(args[1]);
                } catch (NumberFormatException e)
                {

                }
            }
            printHelp(entityPlayer, p);
            return true;
        }
        else
        {
            int radius = 100;

            //Get radius from player
            if (args.length >= 2 && args[1] != null)
            {
                try
                {
                    radius = Integer.parseInt(args[1]);
                    if (radius > 1000)
                    {
                        entityPlayer.addChatMessage(new ChatComponentText("To prevent lag/crashes radius is limited to 1000"));
                        return true;
                    }
                    else if (radius < 1)
                    {
                        entityPlayer.addChatMessage(new ChatComponentText("Radius needs to be positive"));
                        return true;
                    }
                } catch (NumberFormatException e)
                {
                    entityPlayer.addChatMessage(new ChatComponentText("Radius needs to be an integer"));
                    return true;
                }
            }

            EntitySelector selector = null;

            //Find out what entity selector to use
            if (args[0].equalsIgnoreCase("projectiles"))
            {
                selector = EntitySelectors.PLAYER_SELECTOR.selector();
            }
            else if (args[0].equalsIgnoreCase("mobs"))
            {
                selector = EntitySelectors.MOB_SELECTOR.selector();
            }
            else if (args[0].equalsIgnoreCase("living"))
            {
                selector = EntitySelectors.LIVING_SELECTOR.selector();
            }
            else if (args[0].equalsIgnoreCase("items"))
            {
                selector = EntitySelectors.ITEM_SELECTOR.selector();
            }

            if (selector != null)
            {
                List<Entity> list = selector.getEntities(entityPlayer, radius);
                for (Entity entity : list)
                {
                    entity.setDead();
                }
                entityPlayer.addChatMessage(new ChatComponentText("Removed " + list.size() + " " + args[1] + " entities within " + radius + " block radius"));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void printHelp(ICommandSender sender, int p)
    {
        if (sender instanceof EntityPlayer)
        {
            sender.addChatMessage(new ChatComponentText("/" + super_command + " " + getCommandName() + " remove projectiles <radius>"));
            sender.addChatMessage(new ChatComponentText("/" + super_command + " " + getCommandName() + " remove mobs <radius>"));
            sender.addChatMessage(new ChatComponentText("/" + super_command + " " + getCommandName() + " remove all <radius>"));
        }
    }
}
