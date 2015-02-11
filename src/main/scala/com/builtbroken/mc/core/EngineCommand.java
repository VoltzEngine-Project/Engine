package com.builtbroken.mc.core;

import com.builtbroken.mc.prefab.entity.EntityProjectile;
import com.builtbroken.mc.prefab.entity.selector.EntitySelector;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
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
                handleRemoveCommand(entityPlayer, args);
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
        List<Entity> list = EntitySelectors.MOB_SELECTOR.selector().getEntities(entityPlayer, radius);
        for(Entity entity: list)
        {
            entity.setDead();
        }
        entityPlayer.addChatComponentMessage(new ChatComponentText("Removed " + list.size() + " mobs entities within " + radius + " block radius."));
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

    protected void handleRemoveCommand(EntityPlayer entityPlayer, String[] args)
    {
        if (args.length == 1 || args[1].equalsIgnoreCase("help"))
        {
            entityPlayer.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove projectiles <radius>"));
            entityPlayer.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove mobs <radius>"));
            entityPlayer.addChatMessage(new ChatComponentText("/" + getCommandName() + " remove all <radius>"));
        }
        else
        {
            int radius = 100;

            //Get radius from player
            if (args.length >= 2 && args[2] != null)
            {
                try
                {
                    radius = Integer.parseInt(args[2]);
                    if(radius > 1000)
                    {
                        entityPlayer.addChatMessage(new ChatComponentText("To prevent lag/crashes radius is limited to 1000"));
                        return;
                    }
                }
                catch (NumberFormatException e)
                {
                    entityPlayer.addChatMessage(new ChatComponentText("Radius needs to be an integer"));
                    return;
                }
            }

            EntitySelector selector = null;

            //Find out what entity selector to use
            if (args[1].equalsIgnoreCase("projectiles"))
            {
                selector = EntitySelectors.PLAYER_SELECTOR.selector();
            }
            else if (args[1].equalsIgnoreCase("mobs"))
            {
                selector = EntitySelectors.MOB_SELECTOR.selector();
            }
            else if (args[1].equalsIgnoreCase("living"))
            {
                selector = EntitySelectors.LIVING_SELECTOR.selector();
            }
            else if(args[1].equalsIgnoreCase("items"))
            {
                selector = EntitySelectors.ITEM_SELECTOR.selector();
            }

            if(selector == null)
            {
                entityPlayer.addChatMessage(new ChatComponentText("Not sure what you are trying to remove?"));
            }
            else
            {
                List<Entity> list = selector.getEntities(entityPlayer, radius);
                for(Entity entity: list)
                {
                    entity.setDead();
                }
                entityPlayer.addChatMessage(new ChatComponentText("Removed " + list.size() + " " + args[1] + " entities within " + radius + " block radius"));
            }
        }
    }
}
