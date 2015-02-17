package com.builtbroken.mc.core.commands.sub;

import com.builtbroken.mc.prefab.commands.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVEClear extends SubCommand
{
    public CommandVEClear()
    {
        super("clear");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args)
    {
        if(!handleHelp(entityPlayer, args))
        {
            EntityPlayer p;
            if (args.length > 1)
                p = getPlayer(entityPlayer, args[1]);
            else
                p = entityPlayer;
            if (p != null)
            {
                if (args[0].equalsIgnoreCase("armor"))
                {
                    for (int slot = 0; slot < p.inventory.armorInventory.length; slot++)
                    {
                        p.inventory.armorInventory[slot] = null;
                    }
                    p.inventoryContainer.detectAndSendChanges();
                    if (p != entityPlayer)
                        p.addChatComponentMessage(new ChatComponentText("Your armor has been removed by an admin"));
                    else
                        p.addChatComponentMessage(new ChatComponentText("Your armor has been removed"));
                    return true;
                }
                else if (args[0].equalsIgnoreCase("inv"))
                {
                    for (int slot = 0; slot < p.inventory.mainInventory.length; slot++)
                    {
                        p.inventory.mainInventory[slot] = null;
                    }
                    p.inventoryContainer.detectAndSendChanges();
                    if (p != entityPlayer)
                        p.addChatComponentMessage(new ChatComponentText("Your inventory has been cleared by an admin"));
                    else
                        p.addChatComponentMessage(new ChatComponentText("Your inventory has been cleared"));
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("armor");
        items.add("inv");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args != null && args.length > 0 && args[0] != null)
        {
            if (args[0].equalsIgnoreCase("inv") || args[0].equalsIgnoreCase("armor"))
            {
                return args.length == 2 ? getListOfStringsMatchingLastWord(args, this.playersOnlineByUsername()) : null;
            }
        }
        return null;
    }
}
