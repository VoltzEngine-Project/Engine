package com.builtbroken.mc.core;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.prefab.entity.EntityProjectile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 1/23/2015.
 */
public class EngineCommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "ve";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/ve help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args == null || args.length == 0 || args[0].equalsIgnoreCase("help"))
        {
            sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " version"));
            sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove help"));
            sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " butcher"));
            //sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " gridinfo"));
            //sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " gridpause"));
        }
        else if (args[0].equalsIgnoreCase("version"))
        {
            sender.addChatMessage(new ChatComponentText("Version: " + References.VERSION + "  Build: " + References.BUILD_VERSION));
        }
        else if (args[0].equalsIgnoreCase("remove"))
        {
            if (args.length == 1 || args[1].equalsIgnoreCase("help"))
            {
                sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove projectiles <radius>"));
            }
            else if (sender instanceof EntityPlayer)
            {
                int radius = 100;
                EntityPlayer entityPlayer = (EntityPlayer) sender;

                if(args.length >= 2 && args[2] != null)
                {
                    try
                    {
                        radius = Integer.parseInt(args[2]);
                    }
                    catch(NumberFormatException e)
                    {
                        sender.addChatMessage(new ChatComponentText("Radius needs to be an integer"));
                    }
                }
                if (args[1].equalsIgnoreCase("projectiles"))
                {
                    int m = 0;
                    for (Entity entity : getEntitiesWithin(entityPlayer, radius))
                    {
                        if (entity instanceof EntityProjectile)
                        {
                            m++;
                            entity.setDead();
                        }
                    }

                    sender.addChatMessage(new ChatComponentText("Removed " + m + " projectile entities within " + radius + " block radius."));
                }
                else if (args[1].equalsIgnoreCase("mobs"))
                {
                    butcher((EntityPlayer) sender, 100);
                }
                else if(args[1].equalsIgnoreCase("all"))
                {
                    int m = 0;
                    for (Entity entity : getEntitiesWithin(entityPlayer, radius))
                    {
                        if (entity instanceof EntityLiving)
                        {
                            m++;
                            entity.setDead();
                        }
                    }

                    sender.addChatMessage(new ChatComponentText("Removed " + m + " living entities within " + radius + " block radius."));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText("Not sure what your trying to remove?"));
                }
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Only a player can trigger remove commands at the moment"));
            }
        }
        else if (args[0].equalsIgnoreCase("butcher"))
        {
            if (sender instanceof EntityPlayer)
            {
                butcher((EntityPlayer) sender, 100);
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Only a player can trigger this command"));
            }
        }
        else
        {
            throw new WrongUsageException(this.getCommandUsage(sender));
        }
    }

    public void butcher(EntityPlayer entityPlayer, double radius)
    {
        int m = 0;
        for (Entity entity : getEntitiesWithin(entityPlayer, radius))
        {
            if (entity instanceof IMob)
            {
                m++;
                entity.setDead();
            }
        }

        entityPlayer.addChatComponentMessage(new ChatComponentText("Removed " + m + " mobs entities within " + radius + " block radius."));
    }

    private List<Entity> getEntitiesWithin(EntityPlayer entityPlayer, double radius)
    {
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(entityPlayer.posX - radius, entityPlayer.posY - radius, entityPlayer.posZ - radius, entityPlayer.posX + radius, entityPlayer.posY + radius, entityPlayer.posZ + radius);
        return entityPlayer.worldObj.getEntitiesWithinAABB(Entity.class, bounds);
    }
}
