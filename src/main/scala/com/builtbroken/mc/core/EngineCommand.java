package com.builtbroken.mc.core;

import com.builtbroken.mc.prefab.entity.EntityProjectile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
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
            if (sender instanceof EntityPlayer)
            {
                sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove help"));
                sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " butcher"));
            }
            //sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " gridinfo"));
            //sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " gridpause"));
        }
        else if (args[0].equalsIgnoreCase("version"))
        {
            sender.addChatMessage(new ChatComponentText("Version: " + References.VERSION + "  Build: " + References.BUILD_VERSION));
        }
        else if (sender instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) sender;
            if (args[0].equalsIgnoreCase("remove"))
            {
                if (args.length == 1 || args[1].equalsIgnoreCase("help"))
                {
                    sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove projectiles <radius>"));
                    sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove mobs <radius>"));
                    sender.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove all <radius>"));
                }
                else
                {
                    int radius = 100;

                    if (args.length >= 2 && args[2] != null)
                    {
                        try
                        {
                            radius = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e)
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
                        butcher(entityPlayer, 100);
                    }
                    else if (args[1].equalsIgnoreCase("all"))
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
                        sender.addChatMessage(new ChatComponentText("Not sure what you are trying to remove?"));
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("butcher"))
            {
                butcher((EntityPlayer) sender, 100);
            }
            else if(args[0].equalsIgnoreCase("clearinv"))
            {
                EntityPlayer p;
                if(args.length > 1)
                    p = getPlayer(sender, args[1]);
                else
                    p = entityPlayer;

                if(p != null)
                {
                    for (int slot = 0; slot < p.inventory.mainInventory.length; slot++)
                    {
                        p.inventory.mainInventory[slot] = null;
                    }
                    p.inventoryContainer.detectAndSendChanges();
                }
            }
            else if(args[0].equalsIgnoreCase("cleararmor"))
            {

                EntityPlayer p;
                if(args.length > 1)
                    p = getPlayer(sender, args[1]);
                else
                    p = entityPlayer;

                if(p != null)
                {
                    for (int slot = 0; slot < p.inventory.armorInventory.length; slot++)
                    {
                        p.inventory.armorInventory[slot] = null;
                    }
                    p.inventoryContainer.detectAndSendChanges();
                }
            }
            else
            {
                throw new WrongUsageException(this.getCommandUsage(sender));
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

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if(args != null && args.length > 0 && args[0] != null)
        {
            if (sender instanceof EntityPlayer)
            {
                if (args[0].equalsIgnoreCase("clearinv"))
                {
                    return args.length == 2 ? getListOfStringsMatchingLastWord(args, this.userNames()) : null;
                }
            }
        }
        return null;
    }

    protected String[] userNames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    private List<Entity> getEntitiesWithin(EntityPlayer entityPlayer, double radius)
    {
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(entityPlayer.posX - radius, entityPlayer.posY - radius, entityPlayer.posZ - radius, entityPlayer.posX + radius, entityPlayer.posY + radius, entityPlayer.posZ + radius);
        return entityPlayer.worldObj.getEntitiesWithinAABB(Entity.class, bounds);
    }
}
